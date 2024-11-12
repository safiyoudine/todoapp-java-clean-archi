package com.todoapp.business.task.usecases;

import com.todoapp.business.task.infra.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteTaskTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private DeleteTask deleteTask;

    private Long taskId;

    @BeforeEach
    public void setUp() {
        taskId = 1L;  // ID de la tâche pour les tests
    }

    @Test
    public void testExecute_taskExists() {
        // Cas où la tâche existe et doit être supprimée
        doNothing().when(taskRepository).delete(taskId);  // Simulation de la suppression sans erreur

        deleteTask.execute(taskId);

        verify(taskRepository).delete(taskId);  // Vérifie que la méthode delete() a bien été appelée
    }

    @Test
    public void testExecute_taskNotFound() {
        // Cas où la tâche n'existe pas et la méthode delete() ne fait rien
        doNothing().when(taskRepository).delete(taskId);  // Simulation de la suppression sans erreur

        deleteTask.execute(taskId);

        verify(taskRepository).delete(taskId);  // Vérifie que la méthode delete() a bien été appelée, même si la tâche n'existe pas
    }
}
