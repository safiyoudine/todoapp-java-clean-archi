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
        taskId = 1L;
    }

    @Test
    public void testExecute_taskExists() {

        doNothing().when(taskRepository).delete(taskId);

        deleteTask.execute(taskId);

        verify(taskRepository).delete(taskId);
    }

    @Test
    public void testExecute_taskNotFound() {

        doNothing().when(taskRepository).delete(taskId);

        deleteTask.execute(taskId);

        verify(taskRepository).delete(taskId);
    }
}
