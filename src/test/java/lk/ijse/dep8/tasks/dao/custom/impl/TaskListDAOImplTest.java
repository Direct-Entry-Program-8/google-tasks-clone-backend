package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.entity.TaskList;
import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskListDAOImplTest {

    private Session session;
    private TaskListDAOImpl taskListDao;

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        taskListDao = new TaskListDAOImpl(session);
        session.beginTransaction();
        session.save(new User("U001","dulanga@ijse.lk", "abc", "Dulanga", null));
    }

    @Test
    void save() {
        // given
        TaskList givenTaskList = new TaskList("To-do",
                session.load(User.class, "U001"));
        // when
        TaskList actualTaskList = taskListDao.save(givenTaskList);
        // then
        assertEquals(givenTaskList, actualTaskList);
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

    @Test
    void existsById() {
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
}