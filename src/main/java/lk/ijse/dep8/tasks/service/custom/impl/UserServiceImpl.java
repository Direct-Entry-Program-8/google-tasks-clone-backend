package lk.ijse.dep8.tasks.service.custom.impl;

import lk.ijse.dep8.tasks.dao.custom.UserDAO;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.entity.User;
import lk.ijse.dep8.tasks.service.custom.UserService;
import lk.ijse.dep8.tasks.service.exception.FailedExecutionException;
import lk.ijse.dep8.tasks.service.util.EntityDTOMapper;
import lk.ijse.dep8.tasks.service.util.JPAUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.servlet.http.Part;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

@Scope("prototype")
@Component
public class UserServiceImpl implements UserService {

    private final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    @Autowired
    private UserDAO userDAO;

    public boolean existsUser(String userIdOrEmail) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            userDAO.setEntityManager(em);
            return userDAO.existsUserByEmailOrId(userIdOrEmail);
        } finally {
            em.close();
        }
    }

    public UserDTO registerUser(Part picture,
                                String appLocation,
                                UserDTO user) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            user.setId(UUID.randomUUID().toString());

            if (picture != null) {
                user.setPicture(user.getPicture() + user.getId());
            }
            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));

            userDAO.setEntityManager(em);
            // DTO -> Entity
            User userEntity = EntityDTOMapper.getUser(user);
            User savedUser = userDAO.save(userEntity);
            // Entity -> DTO
            user = EntityDTOMapper.getUserDTO(savedUser);

            if (picture != null) {
                Path path = Paths.get(appLocation, "uploads");
                if (Files.notExists(path)) {
                    Files.createDirectory(path);
                }

                String picturePath = path.resolve(user.getId()).toAbsolutePath().toString();
                picture.write(picturePath);
            }

            em.getTransaction().commit();
            return user;
        } catch (Throwable t) {
            if (em != null && em.getTransaction() != null) {
                em.getTransaction().rollback();
            }
            throw new FailedExecutionException("Failed to save the user", t);
        } finally {
            em.close();
        }
    }

    public UserDTO getUser(String userIdOrEmail) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            userDAO.setEntityManager(em);
            Optional<User> userWrapper = userDAO.findUserByIdOrEmail(userIdOrEmail);
            return EntityDTOMapper.getUserDTO(userWrapper.orElse(null));
        } finally {
            em.close();
        }
    }

    public void deleteUser(String userId, String appLocation) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();
            userDAO.setEntityManager(em);
            userDAO.deleteById(userId);
            em.getTransaction().commit();

            new Thread(() -> {
                Path imagePath = Paths.get(appLocation, "uploads",
                        userId);
                try {
                    Files.deleteIfExists(imagePath);
                } catch (IOException e) {
                    logger.warning("Failed to delete the image: " + imagePath.toAbsolutePath());
                }
            }).start();
        } catch (Throwable t) {
            if (em != null && em.getTransaction() != null) em.getTransaction().rollback();
            throw new FailedExecutionException("Failed to delete the user", t);
        } finally {
            em.close();
        }

    }

    public void updateUser(UserDTO user, Part picture,
                           String appLocation) {
        EntityManager em = JPAUtil.getEntityManagerFactory().createEntityManager();
        try {
            em.getTransaction().begin();

            user.setPassword(DigestUtils.sha256Hex(user.getPassword()));

            userDAO.setEntityManager(em);

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

            em.getTransaction().commit();
        } catch (Throwable e) {
            if (em != null && em.getTransaction() != null) em.getTransaction().rollback();
            throw new FailedExecutionException("Failed to update the user", e);
        } finally {
            em.close();
        }
    }

}
