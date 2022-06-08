package lk.ijse.dep8.tasks.service.util;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HibernateUtilTest {

    @Test
    void getSessionFactory() {
        // given

        // when
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session session = sf.openSession();

        // then
        assertNotNull(sf);
        assertNotNull(session);
    }
}