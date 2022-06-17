package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.QueryDAO;
import lk.ijse.dep8.tasks.entity.CustomEntity;
import org.hibernate.Session;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.sql.Connection;

@Scope("prototype")
@Component
public class QueryDAOImpl implements QueryDAO {

    private Session session;

    public QueryDAOImpl(@Nullable Session session) {
        this.session = session;
    }

    @Override
    public CustomEntity getTaskListInformation(int taskListId) {
        return session.createQuery("SELECT new lk.ijse.dep8.tasks.entity.CustomEntity(tl.id, tl.name, tl.user.fullName) FROM TaskList tl INNER JOIN tl.user WHERE tl.id = ?1",
                        CustomEntity.class)
                .setParameter(1, taskListId).uniqueResult();
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }
}
