package lk.ijse.dep8.tasks;

import lk.ijse.dep8.tasks.config.AppConfig;
import lk.ijse.dep8.tasks.config.JPAConfig;
import lk.ijse.dep8.tasks.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig({TestConfig.class, JPAConfig.class, AppConfig.class})
@ActiveProfiles("test")
public class EntityManagerFactoryTest {

    @Autowired
    private EntityManagerFactory emf;

    @PersistenceUnit
    private EntityManagerFactory emf2;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testEntityManager1(){
        System.out.println(emf);
        assertNotNull(emf);
        System.out.println(emf2);
        assertNotNull(emf2);
        System.out.println(em);
        assertNotNull(em);
    }
}
