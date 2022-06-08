package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.entity.Task;
import lk.ijse.dep8.tasks.entity.TaskList;
import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskDAOImplTest {

    private Session session;
    private  TaskDAOImpl taskDAO;
    private int taskListId;

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        taskDAO = new TaskDAOImpl(session);
        session.beginTransaction();

        if (!session.contains(session.get(User.class, "U001"))){
            session.save(new User("U001", "dulanga@ijse.lk", "admin", "Dulanga", null));
            taskListId = (int) session.save(new TaskList("To-do", session.load(User.class, "U001")));
        }
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
        Task givenTask = new Task("Complete the DAO Layer",
                "Something something",
                1, Task.Status.needsAction, session.get(TaskList.class, taskListId));
        // when
        Task actualTask = taskDAO.save(givenTask);
        // then
        assertEquals(givenTask, actualTask);
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