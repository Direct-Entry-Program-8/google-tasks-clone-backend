package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.entity.Task;
import lk.ijse.dep8.tasks.entity.TaskList;
import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.util.HibernateUtil;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskDAOImplTest {

    private Session session;
    private TaskDAOImpl taskDAO;

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        taskDAO = new TaskDAOImpl(session);
        session.beginTransaction();
        session.save(new User("U001", "dulanga@ijse.lk", "admin", "Dulanga", null));
        session.save(new TaskList("To-do", session.load(User.class, "U001")));
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
                1, Task.Status.needsAction, session.load(TaskList.class, 1));
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