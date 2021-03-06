package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.custom.UserDAO;
import lk.ijse.dep8.tasks.dao.exception.DataAccessException;
import lk.ijse.dep8.tasks.dao.custom.impl.UserDAOImpl;
import lk.ijse.dep8.tasks.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOImplTest {

    private static Connection connection;
    private static UserDAO userDAOImpl;

    @BeforeAll
    static void setUp() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep8_tasks", "root", "mysql");
            connection.setAutoCommit(false);
            userDAOImpl = new UserDAOImpl(connection);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    static void tearDown() {
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static List<User> getDummyUsers() {
        List<User> dummies = new ArrayList<>();
        dummies.add(new User("U001", "u001@gmail.com", "admin", "Kasun", "picture1"));
        dummies.add(new User("U002", "u002@gmail.com", "admin", "Nuwan", "picture1"));
        dummies.add(new User("U003", "u003@gmail.com", "admin", "Ruwan", null));
        dummies.add(new User("U004", "u004@gmail.com", "admin", "Supun", null));
        dummies.add(new User("U005", "u005@gmail.com", "admin", "Gayal", "picture1"));
        return dummies;
    }

    @Order(1)
    @MethodSource("getDummyUsers")
    @ParameterizedTest
    void saveUser(User givenUser) {
        // when
        User savedUser = userDAOImpl.save(givenUser);

        // then
        assertEquals(givenUser, savedUser);
        existsUserById(givenUser.getId());
    }

    @Order(2)
    @ValueSource(strings = {"U001", "U002", "U100"})
    @ParameterizedTest
    void existsUserById(String givenUserId) {
        // when
        boolean result = userDAOImpl.existsById(givenUserId);

        // then
        if (givenUserId.equals("U100")) {
            assertFalse(result);
        } else {
            assertTrue(result);
        }
    }

    @Order(3)
    @ValueSource(strings = {"U001", "U002", "U100"})
    @ParameterizedTest
    void findUserById(String givenUserId) {
        // when
        Optional<User> userWrapper = userDAOImpl.findById(givenUserId);

        // then
        if (givenUserId.equals("U100")) {
            assertFalse(userWrapper.isPresent());
        } else {
            assertTrue(userWrapper.isPresent());
        }
    }

    @Order(4)
    @Test
    void findAllUsers() {
        // when
        List<User> allUsers = userDAOImpl.findAll();

        // then
        assertTrue(allUsers.size() >= 5);
    }

    @Order(5)
    @ValueSource(strings = {"U001", "U002", "U100"})
    @ParameterizedTest
    void deleteUserById(String givenUserId) {
        // when
        if (givenUserId.equals("U100")) {
            assertThrows(DataAccessException.class, () -> userDAOImpl.deleteById(givenUserId));
        } else {
            userDAOImpl.deleteById(givenUserId);
        }

        assertFalse(userDAOImpl.existsById(givenUserId));
    }

    @Order(6)
    @Test
    void countUsers() {
        // given
        long count = 5;
        // when
        long result = userDAOImpl.count();
        // then
        assertTrue(result >= 5);
    }
}