package app.controllers;



import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

public class UserController {

    //Methode til at tilføje ruter til javalin
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.post("login", ctx -> login(ctx, connectionPool)); // Definerer en POST-rute til login, der kalder login-metoden
        app.get("logout", ctx -> logout(ctx));// Definerer en GET-rute til logout, der kalder logout-metoden
        app.get("signUp", ctx -> ctx.render("signUp.html"));// get-methoden bruges til at komme fra forside.hmtl til opretBruger.
        app.post("/signUp", ctx -> createuser(ctx, connectionPool));//post-methoden bruges til at lave en ny bruger og gå tilbage til forside.html
    }

    // Metode til at logge brugeren ud
    private static void logout(@NotNull Context ctx) {
        ctx.req().getSession().invalidate();// Invaliderer den aktuelle session (sletter session-data, f.eks. brugerens login-information)

        ctx.redirect("/");// Omdirigerer brugeren til forsiden af applikationen
    }

    // Metode til at håndtere login

    public static void login(Context ctx, ConnectionPool connectionPool){
        // Hent email og password fra login-formularen
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try{
            User user = CupcakeMapper.login(email, password, connectionPool); //Tjekker om brugeren findes i databasen

            ctx.sessionAttribute("currentUser", user);//gemmer brugeren i sessionen(så man ikke skal logge ind på hver side)
            ctx.sessionAttribute("email", user.getUserName());//viser emailen i topmenuen, så man ved hvem der er loggegt in
             //TODO: Tjek om den skal hedde getEmail eller getName, da name er email i db

           /* if(user.isAdmin()){//tjekker om brugeren er admin eller kunde
                ctx.redirect("/admin");// hvis admin så gå til admin siden
            }else{
                ctx.redirect("/shop");//hvis kunde så gå til shop
            }*/
        } catch (DatabaseException e){ // hvis login failer(forkert email eller password), kommer denne besked og siden rendere igen
            ctx.attribute("message", "Forkert email eller password");
            ctx.render("login.html");
        }
    }

    private static void createuser(Context ctx, ConnectionPool connectionPool){
        //Hent email og password fra formularen
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        //Hvis passwordsne macher lav den nye bruger
        if(password1.equals(password2)){
            try{
                //opretter bruger i db
                CupcakeMapper.createuser(email, password1, connectionPool);
                //Sender succes besked
                ctx.attribute("message","Du er nu blevet opretter med email:" + email + "Du kan nu logge ind.");
                //sender brugeren til login.html
                ctx.render("index.html");

            }catch (DatabaseException e){
                //Hvis brugeren allerede findes
                ctx.attribute("message", "Denne email eksistere allerede");
                ctx.render("signUp.html");
            }
        }else{
            //hvis passwordsne ikke er ens
            ctx.attribute("message", "Adgangskoderene matcher ikke");
            ctx.render("signUp.html");
        }
    }
}
