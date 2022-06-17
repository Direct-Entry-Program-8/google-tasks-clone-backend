package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.QueryDAO;
import lk.ijse.dep8.tasks.entity.CustomEntity;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Scope("prototype")
@Component
public class QueryDAOImpl implements QueryDAO {

    private EntityManager em;

    public QueryDAOImpl(@Nullable EntityManager em) {
        this.em = em;
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public CustomEntity getTaskListInformation(int taskListId) {
        return em.createQuery("SELECT new lk.ijse.dep8.tasks.entity.CustomEntity(tl.id, tl.name, tl.user.fullName) FROM TaskList tl INNER JOIN tl.user WHERE tl.id = ?1",
                        CustomEntity.class)
                .setParameter(1, taskListId).getSingleResult();
    }
}
