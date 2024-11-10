package com.todoapp.business.task.infra.mapper;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.controller.request.TaskRequest;
import com.todoapp.business.task.infra.controller.response.TaskDto;
import com.todoapp.business.task.infra.entity.TaskEntity;

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
        //task.setUser(entity.getUser());
        //task.setCategory(entity.getCategory());
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
        return task;
    }

    public static TaskEntity toEntity(Task task) {
        if (task == null) {
            return null;
        }
        TaskEntity entity = new TaskEntity();
        entity.setId(task.getId());
        entity.setTitle(task.getTitle());
        entity.setDescription(task.getDescription());
        entity.setDueDate(task.getDueDate());
        entity.setTaskStatus(task.getTaskStatus());
        //entity.setUser(task.getUser());
        //entity.setCategory(task.getCategory());
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
/*
        if (task.getUser() != null) {
            dto.setUserId(task.getUser().getId());
            dto.setName(task.getUser().getName());
        }

        if (task.getCategory() != null) {
            dto.setCategoryId(task.getCategory().getId());
            dto.setCategoryLabel(task.getCategory().getLabel());
        } */

        return dto;
    }
}
