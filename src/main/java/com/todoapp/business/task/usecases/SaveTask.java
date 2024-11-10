package com.todoapp.business.task.usecases;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.repository.CategoryRepository;
import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.repository.TaskRepository;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SaveTask {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserService userService;

    public Task execute(Task task) {
        Optional<Task> taskOptional = taskRepository.findByTaskId(task.getId());
        Optional<Category> castegoryOptional = categoryRepository.findById(task.getCategory().getId());
        Optional<User> userOptional = userService.getAuthenticatedUsername();
        if (!taskOptional.isPresent() && !castegoryOptional.isPresent() && !userOptional.isPresent()) {
            task.setCategory(castegoryOptional.get());
            task.setUser(userOptional.get());
            return taskRepository.save(task);
        }
        return null;
    }
}
