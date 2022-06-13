package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.CrudDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.TaskListDAO;
import lk.ijse.dep8.tasks.entity.TaskList;
import org.hibernate.Session;

import javax.persistence.EntityManager;

public class TaskListDAOImpl extends CrudDAOImpl<TaskList, Integer> implements TaskListDAO {

    public TaskListDAOImpl(EntityManager em) {
        this.em = em;
    }
}
