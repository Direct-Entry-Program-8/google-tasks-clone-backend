package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.QueryDAO;
import lk.ijse.dep8.tasks.entity.CustomEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.Connection;

@Scope("prototype")
@Repository
public class QueryDAOImpl implements QueryDAO {

    private final SessionFactory sessionFactory;

    public QueryDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public CustomEntity getTaskListInformation(int taskListId) {
        return getSession().createQuery("SELECT new lk.ijse.dep8.tasks.entity.CustomEntity(tl.id, tl.name, tl.user.fullName) FROM TaskList tl INNER JOIN tl.user WHERE tl.id = ?1",
                        CustomEntity.class)
                .setParameter(1, taskListId).uniqueResult();
    }
}
