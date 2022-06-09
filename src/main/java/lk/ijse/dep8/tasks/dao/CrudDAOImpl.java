package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.SuperEntity;
import lk.ijse.dep8.tasks.entity.User;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class CrudDAOImpl<T extends SuperEntity, ID extends Serializable>
        implements CrudDAO<T, ID> {

    protected Session session;

    @Override
    public boolean existsById(ID pk) {
        return findById(pk).isPresent();
    }

    @Override
    public T save(T entity) {
        session.save(entity);
        return entity;
    }

    @Override
    public void deleteById(ID pk) {
        //session.delete(session.load(T, pk));
    }

    @Override
    public Optional<T> findById(ID pk) {
//        T entity = session.get(T, pk);
//        return (entity == null) ? Optional.empty() : Optional.of(entity);
    }

    @Override
    public List<T> findAll() {
//        return session.createQuery("FROM T", T.class).list();
    }

    @Override
    public long count() {
//        return session.createQuery("SELECT COUNT(entity) FROM T entity", Long.class).uniqueResult();
    }
}
