package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dto.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.AssertionFailedError;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    private Connection connection;

    @BeforeEach
    void setUp() {
        System.out.println("Before");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep8_tasks", "root", "mysql");
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
        System.out.println("After");
        try {
            connection.rollback();
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"chamma@ijse.lk","dulanga@ijse.lk", "gihara@ijse.lk", "e5468373-8413-43f8-8484-bcecdbb93e99"})
    void existsUser(String arg) throws SQLException {
        boolean result = UserDAO.existsUser(connection, arg);
        assertTrue(result);
    }


    @Test
    void saveUser() throws SQLException {
        String id = UUID.randomUUID().toString();
        UserDTO givenUser = new UserDTO(id, "Kasun", "kasun@gmail.com", "abc", null);
        UserDTO savedUser = UserDAO.saveUser(connection, givenUser);
        boolean result = UserDAO.existsUser(connection, savedUser.getEmail());
        assertTrue(result);
        assertEquals(givenUser, savedUser);
    }

    @ParameterizedTest
    @ValueSource(strings = {"acb","dulanga@ijse.lk", "gihara@ijse.lk", "e5468373-8413-43f8-8484-bcecdbb93e99"})
    void getUser(/* Given */String value) throws SQLException {
        // When
        UserDTO user = UserDAO.getUser(connection, value);
        // Then
        assertNotNull(user);
    }


    @Test
    void deleteUser() throws SQLException {
        // Given
        String userId = "e5468373-8413-43f8-8484-bcecdbb93e99";
        // When
        UserDAO.deleteUser(connection, userId);
        // Then
        assertThrows(AssertionFailedError.class, () -> existsUser(userId));
    }

    @Test
    void updateUser() throws SQLException {
        // Given
        UserDTO givenUser = UserDAO.getUser(connection, "dulanga@ijse.lk");
        givenUser.setName("Dulanga DEP");
        givenUser.setPassword("Some Random Password");
        givenUser.setPicture("Crazy Picture");
        // When
        UserDAO.updateUser(connection, givenUser);
        // Then
        UserDTO updatedUser = UserDAO.getUser(connection, "dulanga@ijse.lk");
        assertEquals(givenUser, updatedUser);
    }
}