package com.todoapp.business.task.usecases;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.repository.TaskRepository;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindTaskWithCurrentUserTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private FindTaskWithCurrentUser findTaskWithCurrentUser;

    private Long taskId;
    private Long userId;
    private Task task;
    private Category category;

    private User user;

    @BeforeEach
    public void setUp() {
        taskId = 1L;
        userId = 1L;
        task = new Task();
        task.setId(taskId);
        task.setTitle("Task 1");
        task.setDescription("Description of Task 1");
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setPassword("validPassword");

        category = new Category();
        category.setId(1L);
        category.setLabel("Java");

        task.setUser(user);
    }

    @Test
    public void testExecute_taskFound() {
        task.setUser(user);
        when(userService.getAuthenticatedUsername()).thenReturn(Optional.of(user));
        when(taskRepository.findByTaskId(taskId, userId)).thenReturn(Optional.of(task));

        Task result = findTaskWithCurrentUser.execute(taskId);

        assertNotNull(result, "The task should be found");
        assertEquals(taskId, result.getId(), "The task ID should match");
        verify(taskRepository).findByTaskId(taskId, userId);
    }

    @Test
    public void testExecute_taskNotFound() {
        when(userService.getAuthenticatedUsername()).thenReturn(Optional.of(user));
        when(taskRepository.findByTaskId(taskId, userId)).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> findTaskWithCurrentUser.execute(taskId));

        assertEquals("Task not found with the provided ID", thrown.getMessage(), "The exception message should be correct");
        verify(taskRepository).findByTaskId(taskId, userId);
    }

    @Test
    public void testExecute_userNotFound() {
        when(userService.getAuthenticatedUsername()).thenReturn(Optional.empty());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> findTaskWithCurrentUser.execute(taskId));

        assertEquals("Authenticated user not found", thrown.getMessage(), "The exception message should indicate that the user is not authenticated.");
        verify(taskRepository, never()).findByTaskId(taskId, userId);
    }
}
