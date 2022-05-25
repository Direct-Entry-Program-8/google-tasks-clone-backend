package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.User;

import java.util.Optional;

public interface UserDAO extends SuperDAO {

    boolean existsUserByEmailOrId(String emailOrId);

    Optional<User> findUserByIdOrEmail(String userIdOrEmail);

}
