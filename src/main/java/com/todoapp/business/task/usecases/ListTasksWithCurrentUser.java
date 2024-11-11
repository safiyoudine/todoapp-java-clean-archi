package com.todoapp.business.task.usecases;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.repository.TaskRepository;
import com.todoapp.business.user.infra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class ListTasksWithCurrentUser {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Page<Task> execute(int page, int size) {
        return taskRepository.findByUserId(userService.getAuthenticatedUsername().get().getId(), page, size);
    }
}
