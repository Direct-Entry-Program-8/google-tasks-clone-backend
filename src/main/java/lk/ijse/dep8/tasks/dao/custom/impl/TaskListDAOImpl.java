package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.CrudDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.TaskListDAO;
import lk.ijse.dep8.tasks.entity.TaskList;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Scope("prototype")
@Repository
public class TaskListDAOImpl extends CrudDAOImpl<TaskList, Integer> implements TaskListDAO {

    public TaskListDAOImpl(@Nullable Session session) {
        this.session = session;
    }
}
