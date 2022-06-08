package lk.ijse.dep8.tasks.service.util;

import lk.ijse.dep8.tasks.dto.TaskDTO;
import lk.ijse.dep8.tasks.dto.TaskListDTO;
import lk.ijse.dep8.tasks.dto.UserDTO;
import lk.ijse.dep8.tasks.entity.Task;
import lk.ijse.dep8.tasks.entity.TaskList;
import lk.ijse.dep8.tasks.entity.User;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class EntityDTOMapperTest {

    @Test
    void getUserDTO() {
        // given
        User user = new User(UUID.randomUUID().toString(),
                "dulanga@ijse.lk",
                "my-password",
                "Dulanga",
                "Some Picture");

        // when
        UserDTO userDTO = EntityDTOMapper.getUserDTO(user);

        // then
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFullName(), userDTO.getName());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getProfilePic(), userDTO.getPicture());
        assertEquals(user.getPassword(), userDTO.getPassword());
    }

    @Test
    void getTaskListDTO() {
        // given
        TaskList myList = new TaskList(1, "My List", UUID.randomUUID().toString());

        // when
        TaskListDTO taskListDTO = EntityDTOMapper.getTaskListDTO(myList);

        // then
        assertEquals(myList.getId(), taskListDTO.getId());
        assertEquals(myList.getName(), taskListDTO.getTitle());
        assertEquals(myList.getUserId(), taskListDTO.getUserId());
    }

    @Test
    void getTaskDTO() {
        // given
        Task task = new Task(1, "To-do1", "Something", 0,
                Task.Status.completed, 1);

        // when
        TaskDTO taskDTO = EntityDTOMapper.getTaskDTO(task);

        // then
        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getTaskListId(), taskDTO.getTaskListId());
        assertEquals(task.getPosition(), taskDTO.getPosition());
        assertEquals(task.getDetails(), taskDTO.getNotes());
        assertEquals(task.getStatus().toString(), taskDTO.getStatus());
        assertEquals(task.getTitle(), taskDTO.getTitle());
    }

    @Test
    void getUser() {
        // given
        UserDTO userDTO = new UserDTO(UUID.randomUUID().toString(),
                "dulanga@ijse.lk",
                "ijse", "I am Dula", "MyPic");

        // when
        User user = EntityDTOMapper.getUser(userDTO);

        // then
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFullName(), userDTO.getName());
        assertEquals(user.getPassword(), userDTO.getPassword());
        assertEquals(user.getProfilePic(), userDTO.getPicture());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void getTaskList() {
        // given
        TaskListDTO taskListDTO = new TaskListDTO(1, "My Task List", UUID.randomUUID().toString());

        // when
        TaskList taskList = EntityDTOMapper.getTaskList(taskListDTO);

        // then
        assertEquals(taskList.getId(), taskListDTO.getId());
        assertEquals(taskList.getName(), taskListDTO.getTitle());
        assertEquals(taskList.getUserId(), taskListDTO.getUserId());
    }

    @Test
    void getTask() {
        // given
        TaskDTO taskDTO = new TaskDTO(1, "My Item1", 1, "Some Note Here", "completed", 1);

        // when
        Task task = EntityDTOMapper.getTask(taskDTO);

        // then
        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getTaskListId(), taskDTO.getTaskListId());
        assertEquals(task.getPosition(), taskDTO.getPosition());
        assertEquals(task.getDetails(), taskDTO.getNotes());
        assertEquals(task.getStatus().toString(), taskDTO.getStatus());
        assertEquals(task.getTitle(), taskDTO.getTitle());
    }
}