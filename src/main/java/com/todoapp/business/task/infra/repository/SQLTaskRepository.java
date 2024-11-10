package com.todoapp.business.task.infra.repository;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.entity.TaskEntity;
import com.todoapp.business.task.infra.mapper.TaskMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SQLTaskRepository implements TaskRepository {

    @Autowired
    private EntityManager entityManager;


    @Transactional
    public Optional<Task> findByTaskId(Long id) {
        String sql = "SELECT t FROM task t WHERE t.taskId = :taskId";
        try {
            TaskEntity taskEntity = entityManager.createQuery(sql, TaskEntity.class)
                    .setParameter("taskId", id)
                    .getSingleResult();
            return Optional.of(TaskMapper.toDomain(taskEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Task> findByUserId(Long id) {
        String sql = "SELECT t FROM task t WHERE t.userId = :userId";
        try {
            TaskEntity taskEntity = entityManager.createQuery(sql, TaskEntity.class)
                    .setParameter("userId", id)
                    .getSingleResult();
            return Optional.of(TaskMapper.toDomain(taskEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<Task> findAll() {
        String sql = "SELECT t FROM Task t ORDER BY t.taskId ASC";
        List<TaskEntity> taskEntities = entityManager.createQuery(sql, TaskEntity.class).getResultList();
        return taskEntities.stream()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public Task save(Task task) {
        TaskEntity taskEntity = TaskMapper.toEntity(task);
        if (taskEntity.getId() == null) {
            entityManager.persist(taskEntity);
        } else {
            entityManager.merge(taskEntity);
        }
        return TaskMapper.toDomain(taskEntity);
    }

    @Transactional
    public void delete(Long id) {
        TaskEntity taskEntity = entityManager.find(TaskEntity.class, id);
        if (taskEntity != null) {
            entityManager.remove(taskEntity);
        }
    }
}
