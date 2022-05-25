package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {

    boolean existsUserById(String userId);

    public boolean existsUserByEmailOrId(String emailOrId);

    public User saveUser(User user);

    public void deleteUserById(String userId);

    public Optional<User> findUserById(String userId);

    public Optional<User> findUserByIdOrEmail(String userIdOrEmail);

    public List<User> findAllUsers();

    public long countUsers();
}
