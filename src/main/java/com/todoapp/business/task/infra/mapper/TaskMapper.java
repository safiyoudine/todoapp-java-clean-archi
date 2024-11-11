package com.todoapp.business.task.infra.mapper;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.entity.CategoryEntity;
import com.todoapp.business.category.infra.mapper.CategoryMapper;
import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.domain.TaskStatus;
import com.todoapp.business.task.infra.controller.request.TaskRequest;
import com.todoapp.business.task.infra.controller.request.UpdateTaskRequest;
import com.todoapp.business.task.infra.controller.response.TaskDto;
import com.todoapp.business.task.infra.entity.TaskEntity;
import com.todoapp.business.user.infra.entity.UserEntity;
import com.todoapp.business.user.infra.mapper.UserMapper;

public class TaskMapper {

    // Mapper entre TaskEntity et Task (Domain)
    public static Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        Task task = new Task();
        task.setId(entity.getId());
        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setDueDate(entity.getDueDate());
        task.setTaskStatus(entity.getTaskStatus());
        task.setUser(UserMapper.toDomain(entity.getUserEntity()));
        task.setCategory(CategoryMapper.toDomain(entity.getCategoryEntity()));
        return task;
    }

    public static Task toDomain(TaskRequest request) {
        if (request == null) {
            return null;
        }
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        Category category = new Category();
        category.setId(request.getCategoryId());
        task.setCategory(category);
        task.setTaskStatus(TaskStatus.TODO);
        return task;
    }

    public static Task toDomain(UpdateTaskRequest request) {
        if (request == null) {
            return null;
        }
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setTaskStatus(request.getTaskStatus());
        Category category = new Category();
        category.setId(request.getCategoryId());
        task.setCategory(category);
        return task;
    }

    public static TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        TaskEntity entity = new TaskEntity();
        if (task.getId() != null) {
            entity.setId(task.getId());
        }
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setDueDate(task.getDueDate());
        entity.setTaskStatus(task.getTaskStatus());
        if (task.getUser() != null) {
            UserEntity userEntity = new UserEntity();  // Créez ou recherchez l'entité User en fonction de votre logique
            userEntity.setId(task.getUser().getId()); // Utilisez l'ID ou recherchez dans la base de données si nécessaire
            entity.setUserEntity(userEntity);
        }

        if (task.getCategory() != null) {
            CategoryEntity categoryEntity = new CategoryEntity(); // Créez ou recherchez l'entité Category
            categoryEntity.setId(task.getCategory().getId());
            entity.setCategoryEntity(categoryEntity);
        }
        return entity;
    }

    // Mapper entre Task (Domain) et TaskDto
    public static TaskDto toDto(Task task) {
        if (task == null) {
            return null;
        }
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setTaskStatus(task.getTaskStatus());

        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
            dto.setName(task.getUser().getFirstName() + " " + task.getUser().getLastName());
        }

        if (task.getCategory() != null) {
            dto.setCategoryId(task.getCategory().getId());
            dto.setCategoryLabel(task.getCategory().getLabel());
        }
        return dto;
    }
}
