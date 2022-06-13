package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.QueryDAO;
import lk.ijse.dep8.tasks.entity.CustomEntity;

import javax.persistence.EntityManager;

public class QueryDAOImpl implements QueryDAO {

    private final EntityManager em;

    public QueryDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public CustomEntity getTaskListInformation(int taskListId) {
        return em.createQuery("SELECT new lk.ijse.dep8.tasks.entity.CustomEntity(tl.id, tl.name, tl.user.fullName) FROM TaskList tl INNER JOIN tl.user WHERE tl.id = ?1",
                        CustomEntity.class)
                .setParameter(1, taskListId).getSingleResult();
    }
}
