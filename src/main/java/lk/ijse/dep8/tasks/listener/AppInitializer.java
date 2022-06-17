package lk.ijse.dep8.tasks.listener;

import lk.ijse.dep8.tasks.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    private volatile AnnotationConfigApplicationContext ctx;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();
        sce.getServletContext().setAttribute("ioc", ctx);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ctx.close();
    }
}
