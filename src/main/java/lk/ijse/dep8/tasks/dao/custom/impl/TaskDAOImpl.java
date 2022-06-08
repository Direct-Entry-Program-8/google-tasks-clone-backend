package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.TaskDAO;
import lk.ijse.dep8.tasks.entity.Task;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class TaskDAOImpl implements TaskDAO {

    private Session session;

    public TaskDAOImpl(Session session) {
        this.session = session;
    }

    @Override
    public boolean existsById(Integer pk) {
        return findById(pk).isPresent();
    }

    @Override
    public Task save(Task entity) {
        session.save(entity);
        return entity;
    }

    @Override
    public void deleteById(Integer pk) {
        session.delete(session.load(Task.class, pk));
    }

    @Override
    public Optional<Task> findById(Integer pk) {
        Task task = session.get(Task.class, pk);
        return task == null ? Optional.empty(): Optional.of(task);
    }

    @Override
    public List<Task> findAll() {
        return session.createQuery("FROM Task t", Task.class).list();
    }

    @Override
    public long count() {
        return session.createQuery("SELECT COUNT(t) FROM Task t", Long.class).uniqueResult();
    }
}
