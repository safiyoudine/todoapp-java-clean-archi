package com.todoapp.business.task.usecases;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.repository.TaskRepository;
import com.todoapp.business.user.infra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindTaskWithCurrentUser {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task execute(Long taskId) {
        Long userId = userService.getAuthenticatedUsername().get().getId();
        Optional<Task> taskOptional = taskRepository.findByTaskId(taskId, userId);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found with the provided ID");
        }
        return taskOptional.get();
    }
}
