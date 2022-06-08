package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOImpl2Test {

    private UserDAOImpl2 userDAO;
    private Session session;

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        userDAO = new UserDAOImpl2(session);
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();
        session.close();
    }

    @Test
    void existsById() {
    }

    @Test
    void save() {
        // given
        String userId = UUID.randomUUID().toString();
        User user = new User(userId,
                "dualnga@ijse.lk",
                "admin",
                "Dulanga",
                null);

        // when
        Serializable id = session.save(user);

        // then
        assertTrue(session.contains(user));
        assertEquals(userId, id);
    }

    @Test
    void deleteById() {
    }

    @Test
    void findById() {
    }

    @Test
    void findAll() {
    }

    @Test
    void count() {
    }

    @Test
    void existsUserByEmailOrId() {
    }

    @Test
    void findUserByIdOrEmail() {
    }
}