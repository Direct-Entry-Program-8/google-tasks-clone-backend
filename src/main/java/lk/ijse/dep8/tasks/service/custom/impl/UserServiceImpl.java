package lk.ijse.dep8.tasks.service.custom.impl;

import lk.ijse.dep8.tasks.dao.DAOFactory;
import lk.ijse.dep8.tasks.dao.custom.UserDAO;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

public class UserServiceImpl {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    public boolean existsUser(Connection connection, String userIdOrEmail) throws SQLException {
        UserDAO userDAO = DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOTypes.USER);
        return userDAO.existsUserByEmailOrId(userIdOrEmail);
    }

    public UserDTO registerUser(Connection connection, Part picture,
                                String appLocation,
                                UserDTO user) throws SQLException {
        try {
            connection.setAutoCommit(false);
            user.setId(UUID.randomUUID().toString());

            if (picture != null) {
                user.setPicture(user.getPicture() + user.getId());
            }
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));

            UserDAO userDAO =  DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOTypes.USER);
            // DTO -> Entity
            User userEntity = new User(user.getId(), user.getEmail(), user.getPassword(), user.getName(), user.getPicture());
            User savedUser = userDAO.save(userEntity);
            // Entity -> DTO
            user = new UserDTO(savedUser.getId(), savedUser.getFullName(), savedUser.getEmail(),
                    savedUser.getPassword(), savedUser.getProfilePic());

            if (picture != null) {
                Path path = Paths.get(appLocation, "uploads");
                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }

                String picturePath = path.resolve(user.getId()).toAbsolutePath().toString();
                picture.write(picturePath);
            }

            connection.commit();
            return user;
        } catch (Throwable t) {
            connection.rollback();
            throw new RuntimeException(t);
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public UserDTO getUser(Connection connection, String userIdOrEmail) throws SQLException {
        UserDAO userDAO =  DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOTypes.USER);
        Optional<User> userWrapper = userDAO.findUserByIdOrEmail(userIdOrEmail);
        return userWrapper.map(e -> new UserDTO(e.getId(), e.getFullName(), e.getEmail(),
                e.getPassword(), e.getProfilePic())).orElse(null);
    }

    public void deleteUser(Connection connection, String userId, String appLocation) throws SQLException {
        UserDAO userDAO =  DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOTypes.USER);
        userDAO.deleteById(userId);

        new Thread(() -> {
            Path imagePath = Paths.get(appLocation, "uploads",
                    userId);
            try {
                Files.deleteIfExists(imagePath);
            } catch (IOException e) {
                logger.warning("Failed to delete the image: " + imagePath.toAbsolutePath());
            }
        }).start();
    }

    public void updateUser(Connection connection, UserDTO user, Part picture,
                           String appLocation) throws SQLException {
        try {
            connection.setAutoCommit(false);

            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));

            UserDAO userDAO =  DAOFactory.getInstance().getDAO(connection, DAOFactory.DAOTypes.USER);

            // Fetch the current user
            User userEntity = userDAO.findById(user.getId()).get();

            userEntity.setPassword(user.getPassword());
            userEntity.setFullName(user.getName());
            userEntity.setProfilePic(user.getPicture());

            userDAO.save(userEntity);

            Path path = Paths.get(appLocation, "uploads");
            Path picturePath = path.resolve(user.getId());

            if (picture != null) {
                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }

                Files.deleteIfExists(picturePath);
                picture.write(picturePath.toAbsolutePath().toString());
            } else {
                Files.deleteIfExists(picturePath);
            }

            connection.commit();
        } catch (Throwable e) {
            connection.rollback();
            throw new RuntimeException(e);
        } finally {
            connection.setAutoCommit(true);
        }
    }

}