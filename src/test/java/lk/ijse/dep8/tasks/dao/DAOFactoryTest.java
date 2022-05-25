package lk.ijse.dep8.tasks.dao;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DAOFactoryTest {

    @Test
    void getInstance() {
        DAOFactory instance1 = DAOFactory.getInstance();
        DAOFactory instance2 = DAOFactory.getInstance();

        assertEquals(instance1, instance2);
    }
}