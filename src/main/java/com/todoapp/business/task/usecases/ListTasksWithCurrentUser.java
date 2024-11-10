package com.todoapp.business.task.usecases;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.repository.TaskRepository;
import com.todoapp.business.user.infra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListTasksWithCurrentUser {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public List<Task> execute() {
        return taskRepository.findByUserId(
                userService.getAuthenticatedUsername()
                        .get()
                        .getId()
        ).stream().toList();
    }
}
