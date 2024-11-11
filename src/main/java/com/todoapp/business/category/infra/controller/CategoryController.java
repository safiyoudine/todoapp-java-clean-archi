package com.todoapp.business.category.infra.controller;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.controller.request.CategoryRequest;
import com.todoapp.business.category.infra.controller.response.CategoryDto;
import com.todoapp.business.category.infra.mapper.CategoryMapper;
import com.todoapp.business.category.usecases.CreateCategory;
import com.todoapp.business.category.usecases.ListCategories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CreateCategory createCategory;

    @Autowired
    private ListCategories listCategories;


    @GetMapping("/categories")
    public ResponseEntity<?> getCategories() {
        List<Category> categories = listCategories.execute();
        categories.stream().map(CategoryMapper::toDto).forEach(category -> ResponseEntity.ok().body(category));
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PostMapping("/category")
    public ResponseEntity<?> createTask(@RequestBody CategoryRequest categoryRequest) {
        Category category = CategoryMapper.toDomain(categoryRequest);
        CategoryDto categoryDto = CategoryMapper.toDto(createCategory.execute(category));
        if (categoryDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDto);
    }
}
