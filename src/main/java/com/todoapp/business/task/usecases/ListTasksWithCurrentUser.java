package com.todoapp.business.task.usecases;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.repository.TaskRepository;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ListTasksWithCurrentUser {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Page<Task> execute(int page, int size) {
        Optional<User> userOptional = userService.getAuthenticatedUsername();
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("Authenticated user not found");
        }
        Long userId = userOptional.get().getId();
        return taskRepository.findByUserId(userId, page, size);
    }
}
