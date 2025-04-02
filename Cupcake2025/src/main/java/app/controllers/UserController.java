package app.controllers;


import app.entities.Bottom;
import app.entities.Orders;
import app.entities.Topping;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserController {

    //Methode til at tilføje ruter til javalin
    public static void addRoutes(Javalin app, ConnectionPool connectionPool) {
        app.get("login", ctx -> ctx.render("login.html"));
        app.post("/login", ctx -> login(ctx, connectionPool)); // Definerer en POST-rute til login, der kalder login-metoden
        app.get("logout", ctx -> logout(ctx));// Definerer en GET-rute til logout, der kalder logout-metoden
        app.get("signUp", ctx -> ctx.render("signUp.html"));// get-methoden bruges til at komme fra forside.hmtl til opretBruger.
        app.post("/signUp", ctx -> createuser(ctx, connectionPool));//post-methoden bruges til at lave en ny bruger og gå tilbage til forside.html
        app.get("/inspiration", ctx -> ctx.render("inspiration.html"));
        app.get("/shoppingcart", ctx -> ctx.render("shoppingcart.html"));
        app.post("/showCupcakes", ctx -> UserController.getCupcakeOptions(ctx, connectionPool));
        app.get("/admin", ctx -> UserController.getUserOptions(ctx, connectionPool));
        app.get("/adminWatchOrders", ctx -> UserController.watchOrders(ctx, connectionPool));
        app.post("/admin", ctx -> ctx.redirect("page2.html"));
        System.out.println("den rammer routes");
    }


    // Metode til at logge brugeren ud
    private static void logout(@NotNull Context ctx) {
        ctx.req().getSession().invalidate();// Invaliderer den aktuelle session (sletter session-data, f.eks. brugerens login-information)

        ctx.redirect("/");// Omdirigerer brugeren til forsiden af applikationen
    }

    // Metode til at håndtere login

    public static void login(Context ctx, ConnectionPool connectionPool) {
        // Hent email og password fra login-formularen
        String email = ctx.formParam("email");
        String password = ctx.formParam("password");

        try {
            User user = CupcakeMapper.login(email, password, connectionPool); //Tjekker om brugeren findes i databasen
            if (user == null) {
                ctx.attribute("message", "forkert email eller login");
                ctx.render("login.html");
                return;
            }

            ctx.sessionAttribute("currentUser", user);//gemmer brugeren i sessionen(så man ikke skal logge ind på hver side)
            ctx.sessionAttribute("currentUser", user.getUserName());//viser emailen i topmenuen, så man ved hvem der er loggegt in

            //check if you are admin and sending it to admin front page
            if (user.getRole().equals("admin")) {
                ctx.redirect("/admin");
            } else {
                ctx.redirect("/");
            }
            //TODO: Tjek om den skal hedde getEmail eller getName, da name er email i db

           /* if(user.isAdmin()){//tjekker om brugeren er admin eller kunde
                ctx.redirect("/admin");// hvis admin så gå til admin siden
            }else{
                ctx.redirect("/shop");//hvis kunde så gå til shop
            }*/

        } catch (
                DatabaseException e) { // hvis login failer(forkert email eller password), kommer denne besked og siden rendere igen
            ctx.attribute("message", "Forkert email eller password");
            ctx.render("login.html");
        }
    }

    private static void createuser(Context ctx, ConnectionPool connectionPool) {
        //Hent email og password fra formularen
        String email = ctx.formParam("email");
        String password1 = ctx.formParam("password1");
        String password2 = ctx.formParam("password2");

        //Hvis passwordsne macher lav den nye bruger
        if (password1.equals(password2)) {
            try {
                //opretter bruger i db
                CupcakeMapper.createuser(email, password1, connectionPool);
                //Sender succes besked
                ctx.attribute("message", "Du er nu blevet opretter med email:" + email + " Du kan nu logge ind.");
                //sender brugeren til login.html
                ctx.render("/signUp");

            } catch (DatabaseException e) {
                //Hvis brugeren allerede findes
                ctx.attribute("message", "Denne email eksistere allerede");
                ctx.render("signUp.html");
            }
        } else {
            //hvis passwordsne ikke er ens
            ctx.attribute("message", "Adgangskoderene matcher ikke");
            ctx.render("signUp.html");
        }
    }

    public static void getCupcakeOptions(Context ctx, ConnectionPool connectionPool) {

        try {
            List<Topping> toppings = CupcakeMapper.getAllToppings(connectionPool);
            List<Bottom> bottoms = CupcakeMapper.getAllBottoms(connectionPool);

            ctx.attribute("toppings", toppings);
            ctx.attribute("bottoms", bottoms);
            ctx.render("index.html");

        } catch (DatabaseException e) {
            ctx.status(500);
            ctx.attribute("message", "Fejl ved hentning af cupcake data");
            ctx.render("error.html");
        }
    }


    public static void getUserOptions(Context ctx, ConnectionPool connectionPool) {
        try {
            List<User> users = CupcakeMapper.getAllUsers(connectionPool, "postgres");

            ctx.attribute("users", users);
            ctx.render("page1.html");

        } catch (DatabaseException e) {
            ctx.status(500);
            ctx.attribute("message", "Fejl ved hentning af user data");
            ctx.render("error.html");
        }
    }

    public static void watchOrders(Context ctx, ConnectionPool connectionPool){
        try {
            List<Orders> orders = CupcakeMapper.getOrdersByUserId(connectionPool,2);

            ctx.attribute("orders", orders);
            ctx.render("page2.html");

        } catch (DatabaseException e) {
            ctx.status(500);
            ctx.attribute("message", "Fejl ved hentning af order data");
            ctx.render("error.html");
        }

    }


}
