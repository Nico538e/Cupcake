package app.config;

import jakarta.servlet.SessionTrackingMode;
import org.eclipse.jetty.server.session.SessionHandler;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.EnumSet;

public class ThymeleafConfig {
    public static TemplateEngine templateEngine()
    {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/"); // assuming templates are in resources/templates/
        templateResolver.setSuffix(".html");
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }
}
