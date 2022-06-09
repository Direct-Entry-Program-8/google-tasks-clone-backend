package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOImplTest {

    private UserDAOImpl userDAO;
    private Session session;

    static List<User> getDummyUsers() {
        List<User> dummies = new ArrayList<>();
        dummies.add(new User("U001", "u001@gmail.com", "admin", "Kasun", "picture1"));
        dummies.add(new User("U002", "u002@gmail.com", "admin", "Nuwan", "picture1"));
        dummies.add(new User("U003", "u003@gmail.com", "admin", "Ruwan", null));
        dummies.add(new User("U004", "u004@gmail.com", "admin", "Supun", null));
        dummies.add(new User("U005", "u005@gmail.com", "admin", "Gayal", "picture1"));
        return dummies;
    }

    @AfterAll
    static void afterAll() {
        HibernateUtil.getSessionFactory().close();
    }

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        userDAO = new UserDAOImpl(session);
        session.beginTransaction();
    }

    @AfterEach
    void tearDown() {
        session.getTransaction().commit();
        session.close();
    }

    @Order(1)
    @ParameterizedTest
    @MethodSource("getDummyUsers")
    void save(/* given */ User givenUser) {

        // when
        User actualUser = userDAO.save(givenUser);

        // then
        //assertDoesNotThrow(() -> existsById(givenUser));
        assertEquals(givenUser, actualUser);
    }

    @Order(2)
    @ParameterizedTest
    @MethodSource("getDummyUsers")
    void existsById(User givenUser) {
        // given
        String userId = givenUser.getId();

        // when
        boolean result = userDAO.existsById(userId);

        // then
        assertTrue(result);
    }

    @Order(3)
    @ParameterizedTest
    @MethodSource("getDummyUsers")
    void findById(User givenUser) {
        // given
        String userId = givenUser.getId();

        // when
        Optional<User> user = userDAO.findById(userId);

        // then
        assertTrue(user.isPresent());
    }

    @Test
    @Order(4)
    void findAll() {
        // given
        // when
        List<User> users = userDAO.findAll();

        // then
        assertEquals(users, getDummyUsers());
    }

    @Test
    @Order(5)
    void count() {
        // given

        // when
        long count = userDAO.count();

        // then
        assertEquals(getDummyUsers().size(), count);
    }

    @Test
    @Order(6)
    void existsUserByEmailOrId() {
        // given
        String id = "U001";
        String email = "u001@gmail.com";

        // when
        Optional<User> user1 = userDAO.findUserByIdOrEmail(id);
        Optional<User> user2 = userDAO.findUserByIdOrEmail(email);

        // then
        assertTrue(user1.isPresent());
        assertTrue(user2.isPresent());
    }

    @ParameterizedTest
    @MethodSource("getDummyUsers")
    @Order(7)
    void findUserByIdOrEmail(User givenUser) {
        // given
        String idOrEmail = Math.random() < 0.5 ? givenUser.getId() : givenUser.getEmail();

        // when
        Optional<User> actualUser = userDAO.findUserByIdOrEmail(idOrEmail);

        // then
        assertTrue(actualUser.isPresent());
        assertEquals(givenUser, actualUser.get());
    }

    @ParameterizedTest
    @MethodSource("getDummyUsers")
    @Order(8)
    void deleteById(User givenUser) {
        // given
        String userId = givenUser.getId();

        // when, then
        assertDoesNotThrow(() -> {
            userDAO.deleteById(userId);
        });

    }
}