package com.todoapp.business.task.infra.repository;

import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.infra.entity.TaskEntity;
import com.todoapp.business.task.infra.mapper.TaskMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
    public Optional<Task> findByTaskId(Long id, Long userId) {
        String sql = "SELECT t FROM TaskEntity t WHERE t.id = :id AND t.userEntity.id = :userId";
        try {
            TaskEntity taskEntity = entityManager.createQuery(sql, TaskEntity.class)
                    .setParameter("id", id)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return Optional.of(TaskMapper.toDomain(taskEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Page<Task> findByUserId(Long id, int page, int size) {
        String sql = "SELECT t FROM TaskEntity t WHERE t.userEntity.id = :userId";
        int offset = page * size;

        TypedQuery<TaskEntity> query = entityManager.createQuery(sql, TaskEntity.class)
                .setParameter("userId", id)
                .setFirstResult(offset)  // Définir l'offset calculé
                .setMaxResults(size);

        List<TaskEntity> taskEntities = query.getResultList();

        String countQuery = "SELECT COUNT(t) FROM TaskEntity t WHERE t.userEntity.id = :userId";
        Long total = entityManager.createQuery(countQuery, Long.class)
                .setParameter("userId", id)
                .getSingleResult();

        List<Task> tasks = taskEntities.stream()
                .map(TaskMapper::toDomain)
                .collect(Collectors.toList());

        return new PageImpl<>(tasks, PageRequest.of(page, size), total);
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
