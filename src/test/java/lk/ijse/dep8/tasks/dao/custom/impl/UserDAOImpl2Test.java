package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOImpl2Test {

    private UserDAOImpl2 userDAO;
    private Session session;

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        userDAO = new UserDAOImpl2(session);
        session.beginTransaction();
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();
        session.close();
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.getSessionFactory().close();
    }

    @Order(2)
    @Test
    void existsById() {
        // given
        String userId = "U001";

        // when
        boolean u001Exists = userDAO.existsById(userId);
        boolean u002Exists = userDAO.existsById("U002");

        // then
        assertTrue(u001Exists);
        assertFalse(u002Exists);
    }

    @Order(1)
    @Test
    void save() {
        // given
        User givenUser = new User("U001",
                "lahiru@ijse.lk",
                "lahiru",
                "Lahiru",
                null);

        // when
        User actualUser = userDAO.save(givenUser);

        // then
        assertEquals(givenUser, actualUser);
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