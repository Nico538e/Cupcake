package app;

import io.javalin.Javalin;

public class Main {
        public static void main(String[] args)
        {
            // Initializing Javalin and Jetty webserver

            Javalin app = Javalin.create(config -> {
                config.staticFiles.add("/public");
                config.jetty.modifyServletContextHandler(handler ->  handler.setSessionHandler(SessionConfig.sessionConfig()));
                config.fileRenderer(new JavalinThymeleaf(ThymeleafConfig.templateEngine()));
            }).start(7070);

            // Routing

            app.get("/", ctx ->  ctx.render("index.html"));
        }
    }
}