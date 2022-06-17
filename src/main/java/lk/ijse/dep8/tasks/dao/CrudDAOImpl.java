package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.SuperEntity;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class CrudDAOImpl<T extends SuperEntity, ID extends Serializable>
        implements CrudDAO<T, ID> {

    private final Class<T> entityClsObj;
    protected EntityManager em;

    public CrudDAOImpl() {
        entityClsObj = (Class<T>) (((ParameterizedType) (this.getClass().getGenericSuperclass())).getActualTypeArguments()[0]);
    }

    @Override
    public void setEntityManager(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public boolean existsById(ID pk) {
        return findById(pk).isPresent();
    }

    @Override
    public T save(T entity) {
        return em.merge(entity);
    }

    @Override
    public void deleteById(ID pk) {
        em.remove(em.getReference(entityClsObj, pk));
    }

    @Override
    public Optional<T> findById(ID pk) {
        T entity = em.find(entityClsObj, pk);
        return (entity == null) ? Optional.empty() : Optional.of(entity);
    }

    @Override
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClsObj.getName() + " e", entityClsObj).getResultList();
    }

    @Override
    public long count() {
        return em.createQuery("SELECT COUNT(entity) FROM "+ entityClsObj.getName() +" entity", Long.class).getSingleResult();
    }
}
