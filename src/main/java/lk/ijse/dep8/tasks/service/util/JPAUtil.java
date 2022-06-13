package lk.ijse.dep8.tasks.service.util;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.Properties;

public abstract class JPAUtil {

    private static final EntityManagerFactory emf = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        Properties prop = new Properties();
        try {
            String profile = System.getProperty("app.profiles.active", "dev");
            prop.load(JPAUtil.class.getResourceAsStream(
                    profile.equals("dev") ? "/application-dev.properties" : "/application-prod.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Persistence.createEntityManagerFactory("tasks", prop);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return emf;
    }

}
