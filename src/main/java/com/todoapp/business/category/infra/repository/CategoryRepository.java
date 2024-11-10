package com.todoapp.business.category.infra.repository;

import com.todoapp.business.category.domain.Category;

import java.util.Optional;
import java.util.List;

public interface CategoryRepository {

    Optional<Category> findById(Long id);

    Optional<Category> findByLabel(String label);

    List<Category> findAll();

    Category save(Category category);

}
