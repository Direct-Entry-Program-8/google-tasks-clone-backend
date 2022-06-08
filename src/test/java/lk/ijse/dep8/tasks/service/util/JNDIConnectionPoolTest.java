package lk.ijse.dep8.tasks.service.util;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JNDIConnectionPoolTest {

    @RepeatedTest(5)
    void getInstance() {
        JNDIConnectionPool instance1 = JNDIConnectionPool.getInstance();
        JNDIConnectionPool instance2 = JNDIConnectionPool.getInstance();

        assertEquals(instance1, instance2);
    }
}