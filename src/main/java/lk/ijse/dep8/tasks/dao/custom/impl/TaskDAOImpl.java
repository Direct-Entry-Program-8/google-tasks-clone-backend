package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.CrudDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.TaskDAO;
import lk.ijse.dep8.tasks.entity.Task;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Scope("prototype")
@Repository
public class TaskDAOImpl extends CrudDAOImpl<Task, Integer> implements TaskDAO {

    public TaskDAOImpl(@Nullable Session session) {
        this.session = session;
    }
}
