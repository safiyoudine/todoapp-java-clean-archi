package com.todoapp.business.task.usecases;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.repository.CategoryRepository;
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
class SaveTaskTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SaveTask saveTask;

    private Task task;
    private Category category;
    private User user;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setTitle("Task 1");
        task.setDescription("Task 1 Description");

        category = new Category();
        category.setId(1L);
        category.setLabel("Java");

        user = new User();
        user.setId(1L);
        user.setEmail("user1");
        user.setPassword("password123");

        task.setCategory(category); // Associe la tâche à une catégorie
    }

    @Test
    public void testExecute_categoryNotFound() {
        // Cas où la catégorie n'est pas trouvée
        when(categoryRepository.findById(task.getCategory().getId())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> saveTask.execute(task));

        assertEquals("Category not found", thrown.getMessage(), "The exception message should indicate category not found");
        verify(categoryRepository).findById(task.getCategory().getId());  // Vérifie que findById a bien été appelé
    }

    @Test
    public void testExecute_userNotFound() {
        // Cas où l'utilisateur n'est pas authentifié
        when(categoryRepository.findById(task.getCategory().getId())).thenReturn(Optional.of(category));
        when(userService.getAuthenticatedUsername()).thenReturn(Optional.empty());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> saveTask.execute(task));

        assertEquals("Authenticated user not found", thrown.getMessage(), "The exception message should indicate that the user is not authenticated");
        verify(userService).getAuthenticatedUsername();  // Vérifie que getAuthenticatedUsername a bien été appelé
    }

    @Test
    public void testExecute_taskNotFoundForUpdate() {
        // Cas où la tâche à mettre à jour n'est pas trouvée
        task.setId(1L);  // ID d'une tâche à mettre à jour
        when(categoryRepository.findById(task.getCategory().getId())).thenReturn(Optional.of(category));
        when(userService.getAuthenticatedUsername()).thenReturn(Optional.of(user));
        when(taskRepository.findByTaskId(task.getId(), user.getId())).thenReturn(Optional.empty());  // Simule la tâche introuvable

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> saveTask.execute(task));

        assertEquals("Task not found with the provided ID", thrown.getMessage(), "The exception message should indicate task not found for update");
        verify(taskRepository).findByTaskId(task.getId(), user.getId());  // Vérifie que findByTaskId a bien été appelé
    }

    @Test
    public void testExecute_taskSavedSuccessfully() {
        // Cas où la tâche est enregistrée avec succès
        when(categoryRepository.findById(task.getCategory().getId())).thenReturn(Optional.of(category));
        when(userService.getAuthenticatedUsername()).thenReturn(Optional.of(user));
        when(taskRepository.save(task)).thenReturn(task);

        Task result = saveTask.execute(task);

        assertNotNull(result, "The task should be saved.");
        assertEquals("Task 1", result.getTitle(), "The task title should match");
        assertEquals("Task 1 Description", result.getDescription(), "The task description should match");
        verify(taskRepository).save(task);  // Vérifie que save() a bien été appelé
    }
}
