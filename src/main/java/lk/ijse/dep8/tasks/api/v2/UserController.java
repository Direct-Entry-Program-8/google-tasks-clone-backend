package lk.ijse.dep8.tasks.api.v2;

import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.service.custom.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

@RestController
@RequestMapping("v1/users")
public class UserController {

    private final UserService userService;
    private final ServletContext servletContext;

    public UserController(UserService userService, ServletContext servletContext) {
        this.userService = userService;
        this.servletContext = servletContext;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO saveUser(String name, String email, String password, Part picture, HttpServletRequest request) {
        if (name == null || !name.matches("[A-Za-z ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name or name is empty");
        } else if (email == null || !email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or email is empty");
        } else if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password can't be empty");
        } else if (picture != null && (picture.getSize() == 0 || !picture.getContentType().startsWith("image"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid picture");
        }

        if (userService.existsUser(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "A user has been already registered with this email");
        }

        String pictureUrl = null;
        if (picture != null) {
            pictureUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/uploads/";
        }
        UserDTO user = new UserDTO(null, name, email, password, pictureUrl);

        return userService.registerUser(picture, request.getServletContext().getRealPath("/"), user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId:[A-Fa-f0-9\\-]{36}}")
    public void deleteUser(@PathVariable String userId) {
        if (userService.existsUser(userId)) {
            userService.deleteUser(userId, servletContext.getRealPath("/"));
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id");
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{userId:[A-Fa-f0-9\\-]{36}}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateUser(@PathVariable String userId, String name, String email, String password, Part picture, HttpServletRequest request) {
        if (name == null || !name.matches("[A-Za-z ]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name or name is empty");
        } else if (password == null || password.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password can't be empty");
        } else if (picture != null && (picture.getSize() == 0 || !picture.getContentType().startsWith("image"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid picture");
        }

        String pictureUrl = null;
        if (picture != null) {
            pictureUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            pictureUrl += "/uploads/" + userId;
        }

        if (!userService.existsUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id");
        }
        UserDTO user = userService.getUser(userId);
        userService.updateUser(new UserDTO(user.getId(), name, user.getEmail(), password, pictureUrl), picture, servletContext.getRealPath("/"));
    }

    @GetMapping(path = "/{userId:[A-Fa-f0-9\\-]{36}}", produces = "application/json")
    public UserDTO getUser(@PathVariable String userId) {
        if (!userService.existsUser(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id");
        } else {
            return userService.getUser(userId);
        }
    }
}
