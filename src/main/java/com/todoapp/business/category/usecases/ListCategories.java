package com.todoapp.business.category.usecases;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListCategories {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> execute() {
        List<Category> category = categoryRepository.findAll();
        return category.stream()
                .toList();
    }
}
