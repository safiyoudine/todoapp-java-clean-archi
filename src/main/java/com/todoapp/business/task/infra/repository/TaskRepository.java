package com.todoapp.business.task.infra.repository;

import com.todoapp.business.task.domain.Task;
import org.springframework.data.domain.Page;


import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Optional<Task> findByTaskId(Long id, Long userId);

    Page<Task> findByUserId(Long id, int page, int size);

    Task save(Task task);

    void delete(Long id);
}
