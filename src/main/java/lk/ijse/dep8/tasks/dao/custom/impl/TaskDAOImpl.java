package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.CrudDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.TaskDAO;
import lk.ijse.dep8.tasks.entity.Task;
import org.hibernate.Session;

public class TaskDAOImpl extends CrudDAOImpl<Task, Integer> implements TaskDAO {

    private final Session session;

    public TaskDAOImpl(Session session) {
        this.session = session;
    }
}
