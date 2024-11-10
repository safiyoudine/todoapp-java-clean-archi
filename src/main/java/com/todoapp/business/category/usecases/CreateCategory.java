package com.todoapp.business.category.usecases;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreateCategory {

    @Autowired
    private CategoryRepository categoryRepository;


    public Category execute(Category category) {
        Optional<Category> categories = categoryRepository.findByLabel(category.getLabel());
        if (!categories.isPresent()) {
            return categoryRepository.save(category);
        }
        return null;
    }

}
