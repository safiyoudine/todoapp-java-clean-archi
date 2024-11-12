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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListTasksWithCurrentUserTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ListTasksWithCurrentUser listTasksWithCurrentUser;

    private User user;
    private Task task;
    private Category category;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Task 1");
        task.setDescription("Task 1 Description");

        category = new Category();
        category.setId(1L);
        category.setLabel("Java");

        user = new User();
        user.setId(1L);
        user.setEmail("user1");
        user.setPassword("password123");

        task.setCategory(category);
    }

    @Test
    public void testExecute_withAuthenticatedUser() {

        when(userService.getAuthenticatedUsername()).thenReturn(Optional.of(user));

        List<Task> tasks = List.of(task);
        Page<Task> tasksPage = new PageImpl<>(tasks, PageRequest.of(0, 5), 1);

        when(taskRepository.findByUserId(user.getId(), 0, 5)).thenReturn(tasksPage);

        Page<Task> result = listTasksWithCurrentUser.execute(0, 5);

        assertNotNull(result, "The result should not be null.");
        assertEquals(1, result.getTotalElements(), "The total number of tasks should be 1.");
        assertEquals("Task 1", result.getContent().get(0).getTitle(), "The task title should match");
        verify(taskRepository).findByUserId(user.getId(), 0, 5);
    }

    @Test
    public void testExecute_noTasksForUser() {

        when(userService.getAuthenticatedUsername()).thenReturn(Optional.of(user));

        List<Task> tasks = Collections.emptyList();
        Page<Task> tasksPage = new PageImpl<>(tasks, PageRequest.of(0, 5), 0);

        when(taskRepository.findByUserId(user.getId(), 0, 5)).thenReturn(tasksPage);

        Page<Task> result = listTasksWithCurrentUser.execute(0, 5);

        assertNotNull(result, "The result should not be null.");
        assertTrue(result.getContent().isEmpty(), "The result should contain no tasks.");
        verify(taskRepository).findByUserId(user.getId(), 0, 5);
    }

    @Test
    public void testExecute_userNotFound() {

        when(userService.getAuthenticatedUsername()).thenReturn(Optional.empty());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> listTasksWithCurrentUser.execute(0, 5));

        assertEquals("Authenticated user not found", thrown.getMessage(), "The exception message should indicate that the user is not authenticated.");
        verify(taskRepository, never()).findByUserId(anyLong(), anyInt(), anyInt());
    }
}
