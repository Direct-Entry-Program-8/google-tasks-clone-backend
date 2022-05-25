package lk.ijse.dep8.tasks.dao;

import lk.ijse.dep8.tasks.entity.TaskList;

import java.util.List;
import java.util.Optional;

public interface TaskListDAO {

    public boolean existsTaskListById(int listId);

    public TaskList saveTaskList(TaskList taskList);

    public void deleteTaskListById(int listId);

    public Optional<TaskList> findTaskListById(int listId);

    public List<TaskList> findAllTaskList();

    public long countTaskLists();
}
