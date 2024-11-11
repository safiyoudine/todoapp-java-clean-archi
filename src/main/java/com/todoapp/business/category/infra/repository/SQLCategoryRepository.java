package com.todoapp.business.category.infra.repository;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.entity.CategoryEntity;
import com.todoapp.business.category.infra.mapper.CategoryMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class SQLCategoryRepository implements CategoryRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Category> findById(Long id) {
        String sql = "SELECT c FROM CategoryEntity c WHERE c.id = :id";  // Correction en JPQL
        try {
            CategoryEntity categoryEntity = entityManager.createQuery(sql, CategoryEntity.class)
                    .setParameter("id", id)
                    .getSingleResult();
            return Optional.of(CategoryMapper.toDomain(categoryEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<Category> findByLabel(String label) {
        String sql = "SELECT c FROM CategoryEntity c WHERE c.label = :label";  // Correction en JPQL
        try {
            CategoryEntity categoryEntity = entityManager.createQuery(sql, CategoryEntity.class)
                    .setParameter("label", label)
                    .getSingleResult();
            return Optional.of(CategoryMapper.toDomain(categoryEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public List<Category> findAll() {
        String sql = "SELECT c FROM CategoryEntity c";  // Correction en JPQL
        List<CategoryEntity> categoryEntities = entityManager.createQuery(sql, CategoryEntity.class)
                .getResultList();
        return categoryEntities.stream()
                .map(CategoryMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Transactional
    public Category save(Category category) {
        CategoryEntity categoryEntity = CategoryMapper.toEntity(category);
        if (categoryEntity.getId() == null) {
            entityManager.persist(categoryEntity);
        } else {
            entityManager.merge(categoryEntity);
        }
        return CategoryMapper.toDomain(categoryEntity);
    }

}
