package lk.ijse.dep8.tasks.service;

import lk.ijse.dep8.tasks.service.custom.TaskService;
import lk.ijse.dep8.tasks.service.custom.UserService;
import lk.ijse.dep8.tasks.service.custom.impl.TaskServiceImpl;
import lk.ijse.dep8.tasks.service.custom.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceFactoryTest {

    @Mock
    private Connection mockConnection;
    private AutoCloseable autoCloseable;

    @RepeatedTest(2)
    void getInstance() {
        ServiceFactory instance1 = ServiceFactory.getInstance();
        ServiceFactory instance2 = ServiceFactory.getInstance();

        assertEquals(instance1, instance2);
    }

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getService() {
//        Connection mockConnection = mock(Connection.class);
        UserService userService = ServiceFactory.getInstance().
                getService( ServiceFactory.ServiceTypes.USER);
        TaskService taskService = ServiceFactory.getInstance().
                getService( ServiceFactory.ServiceTypes.TASK);

        assertNotNull(userService);
        assertNotNull(taskService);
        assertTrue(userService instanceof UserServiceImpl);
        assertTrue(taskService instanceof TaskServiceImpl);
    }
}