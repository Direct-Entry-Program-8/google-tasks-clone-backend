package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.dao.custom.QueryDAO;
import lk.ijse.dep8.tasks.dao.custom.TaskDAO;
import lk.ijse.dep8.tasks.dao.custom.TaskListDAO;
import lk.ijse.dep8.tasks.dao.custom.UserDAO;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DAOFactoryTest {

    @Test
    void getInstance() {
        DAOFactory instance1 = DAOFactory.getInstance();
        DAOFactory instance2 = DAOFactory.getInstance();

        assertEquals(instance1, instance2);
    }

    @Test
    void getDAO() {
        Connection mockConnection = mock(Connection.class);
        UserDAO userDAO = DAOFactory.getInstance().<UserDAO>
                getDAO(mockConnection, DAOFactory.DAOTypes.USER);
        assertNotNull(userDAO);
        TaskListDAO taskListDAO = DAOFactory.getInstance().<TaskListDAO>
                getDAO(mockConnection, DAOFactory.DAOTypes.TASK_LIST);
        assertNotNull(taskListDAO);
        TaskDAO taskDAO = DAOFactory.getInstance().<TaskDAO>
                getDAO(mockConnection, DAOFactory.DAOTypes.TASK);
        assertNotNull(taskDAO);
        QueryDAO queryDAO = DAOFactory.getInstance().
                getDAO(mockConnection, DAOFactory.DAOTypes.QUERY);
        assertNotNull(queryDAO);
    }
}