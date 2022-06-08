package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.UserDAO;
import lk.ijse.dep8.tasks.entity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UserDAOImpl2 implements UserDAO {

    private final Session session;

    public UserDAOImpl2(Session session) {
        this.session = session;
    }

    @Override
    public boolean existsById(String pk) {
        return findById(pk).isPresent();
    }

    @Override
    public User save(User entity) {
        session.save(entity);
        return entity;
    }

    @Override
    public void deleteById(String pk) {
        session.delete(session.load(User.class, pk));
    }

    @Override
    public Optional<User> findById(String pk) {
        User user = session.get(User.class, pk);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }

    @Override
    public List<User> findAll() {
        return session.createQuery("FROM User u", User.class).list();
    }

    @Override
    public long count() {
        return session.createQuery("SELECT COUNT(u) FROM User u", Long.class).uniqueResult();
    }

    @Override
    public boolean existsUserByEmailOrId(String emailOrId) {
        return findUserByIdOrEmail(emailOrId).isPresent();
    }

    @Override
    public Optional<User> findUserByIdOrEmail(String userIdOrEmail) {
        return session.createQuery("FROM User u WHERE u.id = :id OR u.email = :email", User.class)
                .setParameter("id", userIdOrEmail)
                .setParameter("email", userIdOrEmail)
                .uniqueResultOptional();
    }
}
