package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.CrudDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.TaskListDAO;
import lk.ijse.dep8.tasks.entity.TaskList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
public class TaskListDAOImpl extends CrudDAOImpl<TaskList, Integer> implements TaskListDAO {

    public TaskListDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
