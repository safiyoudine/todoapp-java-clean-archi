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

        Optional<Category> categoryOptional = categoryRepository.findById(task.getCategory().getId());
        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Category not found");
        }
        task.setCategory(categoryOptional.get());

        Optional<User> userOptional = userService.getAuthenticatedUsername();
        if (userOptional.isEmpty()) {
            throw new IllegalStateException("Authenticated user not found");
        }
        task.setUser(userOptional.get());

        if (task.getId() != null) {
            Optional<Task> taskOptional = taskRepository.findByTaskId(task.getId(), userOptional.get().getId());
            if (taskOptional.isEmpty()) {
                throw new IllegalArgumentException("Task not found with the provided ID");
            }
            task.setId(taskOptional.get().getId());
        }

        return taskRepository.save(task);
    }
}
