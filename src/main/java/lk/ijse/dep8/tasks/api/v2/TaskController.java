package lk.ijse.dep8.tasks.api.v2;

import jakarta.json.bind.JsonbException;
import lk.ijse.dep8.tasks.dto.TaskDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("v1/users/{userId:[A-Fa-f0-9\\-]{36}}/lists/{taskListId:\\d+}/tasks")
public class TaskController {

    private final DataSource pool;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public TaskController(DataSource pool) {
        this.pool = pool;
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public TaskDTO saveTask(@PathVariable String userId, @PathVariable int taskListId,
                            @RequestBody TaskDTO task) {
        Connection connection = null;

        try {
            connection = pool.getConnection();

            PreparedStatement stm = connection.
                    prepareStatement("SELECT * FROM task_list t WHERE t.id=? AND t.user_id=?");
            stm.setInt(1, taskListId);
            stm.setString(2, userId);
            if (!stm.executeQuery().next()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid user id or task list id");
            }

            if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid title or title is empty");
            }
            task.setPosition(0);
            task.setStatusAsEnum(TaskDTO.Status.NEEDS_ACTION);

            connection.setAutoCommit(false);
            pushDown(connection, 0, taskListId);

            stm = connection.
                    prepareStatement("INSERT INTO task (title, details, position, status, task_list_id) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, task.getTitle());
            stm.setString(2, task.getNotes());
            stm.setInt(3, task.getPosition());
            stm.setString(4, task.getStatus());
            stm.setInt(5, taskListId);
            if (stm.executeUpdate() != 1) {
                throw new SQLException("Failed to save the task list");
            }

            ResultSet rst = stm.getGeneratedKeys();
            rst.next();
            task.setId(rst.getInt(1));
            connection.commit();

            return task;
        } catch (JsonbException e) {
            throw new ResponseStatusException(400, "Invalid JSON", e);
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    if (!connection.getAutoCommit()) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void pushDown(Connection connection, int pos, int taskListId) throws SQLException {
        PreparedStatement pstm = connection.
                prepareStatement("UPDATE task t SET position = position + 1 WHERE t.position >= ? AND t.task_list_id = ? ORDER BY t.position");
        pstm.setInt(1, pos);
        pstm.setInt(2, taskListId);
        pstm.executeUpdate();
    }

    private void pushUp(Connection connection, int pos, int taskListId) throws SQLException {
        PreparedStatement pstm = connection.
                prepareStatement("UPDATE task t SET position = position - 1 WHERE t.position >= ? AND t.task_list_id = ? ORDER BY t.position");
        pstm.setInt(1, pos);
        pstm.setInt(2, taskListId);
        pstm.executeUpdate();
    }

    @GetMapping(path = "/{taskId:\\d+}", produces = "application/json")
    public TaskDTO getTask(@PathVariable String userId, @PathVariable int taskListId,
                           @PathVariable int taskId) {

        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.
                    prepareStatement("SELECT * FROM task_list tl INNER JOIN task t ON t.task_list_id = tl.id WHERE t.id=? AND tl.id=? AND tl.user_id=?");
            stm.setInt(1, taskId);
            stm.setInt(2, taskListId);
            stm.setString(3, userId);
            ResultSet rst = stm.executeQuery();
            if (rst.next()) {
                String title = rst.getString("title");
                String details = rst.getString("details");
                int position = rst.getInt("position");
                String status = rst.getString("status");
                return new TaskDTO(taskId, title, position, details, status, taskListId);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user id or task list id or task id");
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to fetch task details");
        }

    }

    @GetMapping(produces = "application/json")
    public List<TaskDTO> getAllTasks(@PathVariable String userId, @PathVariable int taskListId) {
        try (Connection connection = pool.getConnection()) {
            PreparedStatement stm = connection.prepareStatement("SELECT * FROM task_list t WHERE t.id=? AND t.user_id=?");
            stm.setInt(1, taskListId);
            stm.setString(2, userId);
            if (!stm.executeQuery().next()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid task list id");
            }

            stm = connection.prepareStatement("SELECT * FROM task WHERE task.task_list_id = ? ORDER BY position");
            stm.setInt(1, taskListId);
            ResultSet rst = stm.executeQuery();

            List<TaskDTO> tasks = new ArrayList<>();
            while (rst.next()) {
                int id = rst.getInt("id");
                String title = rst.getString("title");
                String details = rst.getString("details");
                int position = rst.getInt("position");
                String status = rst.getString("status");
                tasks.add(new TaskDTO(id, title, position, details, status, taskListId));
            }

            return tasks;
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{taskId:\\d+}")
    public void deleteTask(@PathVariable String userId, @PathVariable int taskListId,
                           @PathVariable int taskId) {
        Connection connection = null;
        TaskDTO task = getTask(userId, taskListId, taskId);
        try {
            connection = pool.getConnection();
            connection.setAutoCommit(false);
            pushUp(connection, task.getPosition(), task.getTaskListId());
            PreparedStatement stm = connection.prepareStatement("DELETE FROM task WHERE id=?");
            stm.setInt(1, task.getId());
            if (stm.executeUpdate() != 1) {
                throw new SQLException("Failed to delete the task");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        } finally {
            try {
                if (connection != null) {
                    connection.rollback();
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(path = "/{taskId:\\d+}")
    public void updateTask(@PathVariable String userId, @PathVariable int taskListId,
                           @PathVariable int taskId,
                           @RequestBody TaskDTO newTask) {
        TaskDTO oldTask = getTask(userId, taskListId, taskId);
        Connection connection = null;
        try {

            if (newTask.getTitle() == null || newTask.getTitle().trim().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid title or title is empty");
            } else if (newTask.getPosition() == null || newTask.getPosition() < 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid position or position value is empty");
            }

            connection = pool.getConnection();
            connection.setAutoCommit(false);
            if (!oldTask.getPosition().equals(newTask.getPosition())) {
                pushUp(connection, oldTask.getPosition(), oldTask.getTaskListId());
                pushDown(connection, newTask.getPosition(), oldTask.getTaskListId());
            }

            PreparedStatement stm = connection.
                    prepareStatement("UPDATE task SET title=?, details=?, position=?, status=? WHERE id=?");
            stm.setString(1, newTask.getTitle());
            stm.setString(2, newTask.getNotes());
            stm.setInt(3, newTask.getPosition());
            stm.setString(4, newTask.getStatus());
            stm.setInt(5, oldTask.getId());
            if (stm.executeUpdate() != 1) {
                throw new SQLException("Failed to update the task");
            }

            connection.commit();
        } catch (JsonbException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON");
        } catch (SQLException e) {
            throw new ResponseStatusException(500, e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    if (!connection.getAutoCommit()) {
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}