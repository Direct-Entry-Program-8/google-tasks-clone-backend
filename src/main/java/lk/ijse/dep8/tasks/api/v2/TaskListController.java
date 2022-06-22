package lk.ijse.dep8.tasks.api.v2;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.stream.JsonParser;
import lk.ijse.dep8.tasks.dto.TaskListDTO;
import lk.ijse.dep8.tasks.util.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.StringReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("v1/users/{userId:[A-Fa-f0-9\\-]{36}}/lists")
public class TaskListController {

    private final DataSource pool;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public TaskListController(DataSource pool) {
        this.pool = pool;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
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
    public void updateTaskList(@PathVariable String userId,@PathVariable int taskListId,
                              @RequestBody  TaskListDTO newTaskList){
        TaskListDTO oldTaskList = getTaskListDTO(userId, taskListId);
        if (newTaskList.getTitle() == null || newTaskList.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(400, "Invalid title or title is empty");
        }

        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("UPDATE task_list SET name=? WHERE id=?");
            stm.setString(1, newTaskList.getTitle());
            stm.setInt(2, oldTaskList.getId());
            if (stm.executeUpdate() != 1) {
                throw new SQLException("Failed to update the task list");
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{taskListId:\\d+}")
    public void deleteTaskList(@PathVariable int taskListId){
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("DELETE FROM task_list WHERE id=?");
            stm.setInt(1, taskListId);
            if (stm.executeUpdate() != 1) {
                throw new SQLException("Failed to delete the task list");
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/{taskListId:\\d+}", produces = "application/json")
    public TaskListDTO getTaskList(@PathVariable String userId, @PathVariable int taskListId){
        return getTaskListDTO(userId, taskListId);
    }

    private TaskListDTO getTaskListDTO(String userId, int taskListId) {

        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.
                    prepareStatement("SELECT * FROM task_list t WHERE t.id=? AND t.user_id=?");
            stm.setInt(1, taskListId);
            stm.setString(2, userId);
            ResultSet rst = stm.executeQuery();
            if (rst.next()) {
                int id = rst.getInt("id");
                String title = rst.getString("name");
                return new TaskListDTO(id, title, userId);
            } else {
                throw new ResponseStatusException(404, "Invalid user id or task list id");
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(500, "Failed to fetch task list details");
        }
    }

    @GetMapping(produces = "application/json")
    public List<TaskListDTO> getAllTaskLists(@PathVariable String userId){
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.
                    prepareStatement("SELECT * FROM task_list t WHERE t.user_id=?");
            stm.setString(1, userId);
            ResultSet rst = stm.executeQuery();

            ArrayList<TaskListDTO> taskLists = new ArrayList<>();
            while (rst.next()) {
                int id = rst.getInt("id");
                String title = rst.getString("name");
                taskLists.add(new TaskListDTO(id, title, userId));
            }

           return taskLists;

        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        }
    }
}
