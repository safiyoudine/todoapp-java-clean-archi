package com.todoapp.business.category.infra.mapper;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.controller.request.CategoryRequest;
import com.todoapp.business.category.infra.controller.response.CategoryDto;
import com.todoapp.business.category.infra.entity.CategoryEntity;


public class CategoryMapper {

    // Mapper entre CategoryEntity et Category (Domain)
    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        Category category = new Category();
        category.setId(entity.getId());
        category.setLabel(entity.getLabel());
        return category;
    }

    public static Category toDomain(CategoryRequest request) {
        if (request == null) {
            return null;
        }
        Category category = new Category();
        category.setLabel(request.getLabel());
        return category;
    }

    public static CategoryEntity toEntity(Category category) {
        if (category == null) {
            return null;
        }
        CategoryEntity entity = new CategoryEntity();
        entity.setId(category.getId());
        entity.setLabel(category.getLabel());
        return entity;
    }

    // Mapper entre Category (Domain) et CategoryDto
    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return new CategoryDto(category.getId(), category.getLabel());
    }

}