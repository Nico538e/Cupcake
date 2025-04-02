package app.controllers;



import app.entities.Bottom;
import app.entities.Cupcake;
import app.entities.Topping;
import app.entities.User;
import app.exceptions.DatabaseException;
import app.persistence.ConnectionPool;
import app.persistence.CupcakeMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        app.get("/shoppingCart", ctx -> showCart(ctx, connectionPool));
        app.post("/showCupcakes", ctx -> UserController.getCupcakeOptions(ctx, connectionPool));
        app.post("/shoppingCart", ctx -> addToShoppingCart(ctx,connectionPool));
        app.get("/checkout",ctx -> validatePayment(ctx));

    }

    private static void validatePayment(@NotNull Context ctx) {
        User user = ctx.sessionAttribute("currentUser");

        if(user == null){
            ctx.sessionAttribute("fromBasket", true);
            ctx.redirect("/login");

        }else{
            //træk penge fra database
            //betaling er gået igemmen side(redirct)
        }

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
            if(user == null){
                ctx.attribute("message", "forkert email eller login");
                ctx.render("login.html");
                return;
            }

            ctx.sessionAttribute("currentUser", user);//gemmer brugeren i sessionen(så man ikke skal logge ind på hver side)
            ctx.sessionAttribute("currentUser", user.getUserName());//viser emailen i topmenuen, så man ved hvem der er loggegt in

             //TODO: Tjek om den skal hedde getEmail eller getName, da name er email i db

           /* if(user.isAdmin()){//tjekker om brugeren er admin eller kunde
                ctx.redirect("/admin");// hvis admin så gå til admin siden
            }else{
                ctx.redirect("/shop");//hvis kunde så gå til shop
            }*/

            Boolean fromBasket = ctx.sessionAttribute("fromBasket");

            if(fromBasket == null){
                UserController.getCupcakeOptions(ctx,connectionPool);
            }else{
                ctx.redirect("/shoppingCart");
            }

//session atribute hvis du kommer fra basket ligesom user
            //if statement med om du komer fra basket
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
                ctx.attribute("message","Du er nu blevet oprettet med email: \n" + email + " Du kan nu logge ind.");
                //sender brugeren til login.html
                ctx.render("/signUp");

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

    public static void addToShoppingCart(Context ctx, ConnectionPool connectionPool) throws DatabaseException {
        int bottomId = Integer.parseInt(ctx.formParam("bottom"));
        int toppingId = Integer.parseInt(ctx.formParam("topping"));
        int quantity = Integer.parseInt(ctx.formParam("quantity"));

        Bottom bottom = CupcakeMapper.getOneBottomById(connectionPool, bottomId);
        Topping topping = CupcakeMapper.getOneToppingById(connectionPool, toppingId);


        Cupcake cupcake = new Cupcake(bottom, topping, quantity);

        List<Cupcake> shoppingCart = ctx.sessionAttribute("shoppingCart");

        if(shoppingCart == null){
            shoppingCart = new ArrayList<>();
        }

        shoppingCart.add(cupcake);

        ctx.sessionAttribute("shoppingCart", shoppingCart);

        ctx.redirect("/shoppingCart");
    }

    // Metode til at vise indkøbskurven
    public static void showCart(Context ctx, ConnectionPool connectionPool) {
        // Hent indkøbskurven fra sessionen (hvis den findes)
        List<Cupcake> shoppingCart = ctx.sessionAttribute("shoppingCart");

        // Hvis kurven er tom, opret ny liste
        if (shoppingCart == null) {
            shoppingCart = new ArrayList<>();
        }

        // Beregn totalprisen
        double totalPrice = 0;
        for (Cupcake cupcake : shoppingCart) {
            totalPrice += (cupcake.getTopping().getToppingPrice() + cupcake.getBottom().getBottomPrice()) * cupcake.getQuantity();
        }

        // Tilføj totalprisen og indkøbskurven til viewet
        ctx.attribute("totalPrice", totalPrice);
        ctx.attribute("shoppingCart", shoppingCart);

        // Render shoppingCart siden
        ctx.render("shoppingcart.html");

        //Når de har trykket på knappen skal I kalde på CupcakeMapper.getAmountByID, med userID og connectionPool. Endvidere skal I bagefter sammenligne totalPrice med amout, som bliver returneret fra metoden.
        // der mangler måske lidt kode
        /*
        double amount = CupcakeMapper.getAmountByID(blah lahc blkah);

        if(totalPrice < amount) curly
        double newAmount = amount-totalPrice;
        CupcakeMapper.updateAmountByID(blah blah blha);
        ... renderer siden med at de har købt den..
        curly slut else curly
        renderer en side med at der er ikke nok på deres saldo curly slut
        */

    }

}
