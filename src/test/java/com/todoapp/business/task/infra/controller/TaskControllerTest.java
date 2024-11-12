package com.todoapp.business.task.infra.controller;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.controller.request.TaskRequest;
import com.todoapp.business.task.infra.controller.request.UpdateTaskRequest;
import com.todoapp.business.task.infra.controller.response.TaskDto;
import com.todoapp.business.task.usecases.DeleteTask;
import com.todoapp.business.task.usecases.FindTaskWithCurrentUser;
import com.todoapp.business.task.usecases.ListTasksWithCurrentUser;
import com.todoapp.business.task.usecases.SaveTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SaveTask saveTask;

    @Mock
    private FindTaskWithCurrentUser findTaskWithCurrentUser;

    @Mock
    private DeleteTask deleteTask;

    @Mock
    private ListTasksWithCurrentUser listTasksWithCurrentUser;

    @InjectMocks
    private TaskController taskController;

    private Task task;
    private Task updateTask;
    private TaskDto taskDto;
    private TaskRequest taskRequest;
    private UpdateTaskRequest updateTaskRequest;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();

        task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Description 1");

        updateTask = new Task();
        updateTask.setId(1L);
        updateTask.setTitle("Updated Task 1");
        updateTask.setDescription("Updated Description 1");

        taskDto = new TaskDto();
        taskDto.setId(1L);
        taskDto.setTitle("Task 1");
        taskDto.setDescription("Description 1");

        taskRequest = new TaskRequest();
        taskRequest.setTitle("Task 1");
        taskRequest.setDescription("Description 1");

        updateTaskRequest = new UpdateTaskRequest();
        updateTaskRequest.setTitle("Updated Task 1");
        updateTaskRequest.setDescription("Updated Description 1");
    }

    @Test
    public void testSaveTask_success() throws Exception {

        when(saveTask.execute(any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/api/task")
                        .contentType("application/json")
                        .content("{\"title\":\"Task 1\", \"description\":\"Description 1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));

        verify(saveTask).execute(any(Task.class));
    }

    @Test
    public void testSaveTask_failure() throws Exception {

        when(saveTask.execute(any(Task.class))).thenReturn(null);

        mockMvc.perform(post("/api/task")
                        .contentType("application/json")
                        .content("{\"title\":\"Task 1\", \"description\":\"Description 1\"}"))
                .andExpect(status().isBadRequest());

        verify(saveTask).execute(any(Task.class));
    }

    @Test
    public void testUpdateTask_success() throws Exception {
        when(saveTask.execute(any(Task.class))).thenReturn(updateTask);

        mockMvc.perform(put("/api/task/{taskId}", 1L)
                        .contentType("application/json")
                        .content("{\"title\":\"Task 1\", \"description\":\"Description 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task 1"))
                .andExpect(jsonPath("$.description").value("Updated Description 1"));

        verify(saveTask).execute(any(Task.class));
    }

    @Test
    public void testUpdateTask_notFound() throws Exception {

        when(saveTask.execute(any(Task.class))).thenReturn(null);

        mockMvc.perform(put("/api/task/{taskId}", 1L)
                        .contentType("application/json")
                        .content("{\"title\":\"Updated Task 1\", \"description\":\"Updated Description 1\"}"))
                .andExpect(status().isNotFound());

        verify(saveTask).execute(any(Task.class));
    }

    @Test
    public void testGetAllTasks() throws Exception {

        Page<Task> tasksPage = mock(Page.class);
        when(listTasksWithCurrentUser.execute(0, 5)).thenReturn(tasksPage);

        mockMvc.perform(get("/api/tasks")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk());

        verify(listTasksWithCurrentUser).execute(0, 5);
    }

    @Test
    public void testGetTaskByTaskId() throws Exception {

        when(findTaskWithCurrentUser.execute(1L)).thenReturn(task);

        mockMvc.perform(get("/api/task/{taskId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"))
                .andExpect(jsonPath("$.description").value("Description 1"));

        verify(findTaskWithCurrentUser).execute(1L);
    }

    @Test
    public void testDeleteTask() throws Exception {

        doNothing().when(deleteTask).execute(1L);

        mockMvc.perform(delete("/api/task/{taskId}", 1L))
                .andExpect(status().isNoContent());

        verify(deleteTask).execute(1L);
    }
}
