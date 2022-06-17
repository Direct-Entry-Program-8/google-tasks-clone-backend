package lk.ijse.dep8.tasks.dao.custom.impl;

import lk.ijse.dep8.tasks.dao.CrudDAOImpl;
import lk.ijse.dep8.tasks.dao.custom.UserDAO;
import lk.ijse.dep8.tasks.entity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

@Scope("prototype")
@Component
public class UserDAOImpl extends CrudDAOImpl<User, String> implements UserDAO {

    public UserDAOImpl(@Nullable EntityManager em) {
        this.em = em;
    }

    @Override
    public boolean existsUserByEmailOrId(String emailOrId) {
        return findUserByIdOrEmail(emailOrId).isPresent();
    }

    @Override
    public Optional<User> findUserByIdOrEmail(String userIdOrEmail) {
        try {
            return Optional.of((em.createQuery("SELECT u FROM User u WHERE u.id = :id OR u.email = :email", User.class)
                    .setParameter("id", userIdOrEmail)
                    .setParameter("email", userIdOrEmail)
                    .getSingleResult()));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
