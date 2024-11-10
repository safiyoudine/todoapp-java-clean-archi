package com.todoapp.business.task.usecases;

import com.todoapp.business.task.infra.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteTask {
    @Autowired
    private TaskRepository taskRepository;

    public void execute(Long id) {
        taskRepository.delete(id);
    }
}
