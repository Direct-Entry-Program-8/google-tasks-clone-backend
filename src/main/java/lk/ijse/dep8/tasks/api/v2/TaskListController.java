package lk.ijse.dep8.tasks.api.v2;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import lk.ijse.dep8.tasks.dto.TaskListDTO;
import lk.ijse.dep8.tasks.util.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;

@RestController
@RequestMapping("v1/users/{userId:[A-Fa-f0-9\\-]{36}}/lists")
public class TaskListController {

    private final DataSource pool;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public TaskListController(DataSource pool) {
        this.pool = pool;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public TaskListDTO saveTaskList(@PathVariable String userId,
                             @RequestBody TaskListDTO taskList,
                             HttpServletRequest req){
        try (Connection connection = pool.getConnection()) {

            if (taskList.getTitle() == null || taskList.getTitle().trim().isEmpty()) {
                throw new ResponseStatusException(400, "Invalid title or title is empty");
            }

            PreparedStatement stm = connection.prepareStatement("INSERT INTO task_list (name,user_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, taskList.getTitle());
            stm.setString(2, userId);
            if (stm.executeUpdate() != 1) {
                throw new SQLException("Failed to save the task list");
            }

            ResultSet rst = stm.getGeneratedKeys();
            rst.next();
            taskList.setId(rst.getInt(1));

            return taskList;
        } catch (JsonbException e) {
            throw new ResponseStatusException(400, "Invalid JSON", e);
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{taskListId:\\d+}", consumes = "application/json")
    public void updateTaskList(){}

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{taskListId:\\d+}")
    public void deleteTaskList(){}

    @GetMapping(path = "/{taskListId:\\d+}", produces = "application/json")
    public void getTaskList(){

    }

    @GetMapping(produces = "application/json")
    public void getAllTaskLists(){}
}
