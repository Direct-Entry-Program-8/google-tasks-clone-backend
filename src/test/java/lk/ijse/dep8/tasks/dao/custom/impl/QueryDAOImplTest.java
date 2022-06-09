package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.QueryDAO;
import lk.ijse.dep8.tasks.entity.CustomEntity;
import lk.ijse.dep8.tasks.entity.TaskList;
import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QueryDAOImplTest {

    private QueryDAO queryDAO;
    private Session session;

    @BeforeEach
    void setUp() {
        session = HibernateUtil.getSessionFactory().openSession();
        queryDAO = new QueryDAOImpl(session);
        session.save(new User("U001", "dulanga@ijse.lk", "abc", "Dulanga", null));
        session.save(new TaskList("To-do list", session.load(User.class, "U001")));
    }

    @AfterEach
    void tearDown() {
        session.close();
    }

    @Test
    void getTaskListInformation() {
        // given
        int taskListId = 1;
        // when
        CustomEntity taskListInformation = queryDAO.getTaskListInformation(taskListId);
        // then
        assertEquals(taskListInformation.getTaskListId(), 1);
        assertEquals(taskListInformation.getTaskListName(), "To-do list");
        assertEquals(taskListInformation.getUserName(), "Dulanga");
    }
}