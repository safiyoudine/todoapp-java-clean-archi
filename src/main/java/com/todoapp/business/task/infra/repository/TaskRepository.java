package com.todoapp.business.task.infra.repository;

import com.todoapp.business.task.domain.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findByTaskId(Long id);

    Optional<Task> findByUserId(Long id);

    List<Task> findAll();

    Task save(Task task);

    void delete(Long id);
}
