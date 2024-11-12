package com.todoapp.business.task.usecases;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.repository.TaskRepository;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FindTaskWithCurrentUser {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task execute(Long taskId) {
        Optional<User> userOptional = userService.getAuthenticatedUsername();
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("Authenticated user not found");
        }
        Long userId = userOptional.get().getId();

        Optional<Task> taskOptional = taskRepository.findByTaskId(taskId, userId);
        if (taskOptional.isEmpty()) {
            throw new IllegalArgumentException("Task not found with the provided ID");
        }
        return taskOptional.get();
    }
}
