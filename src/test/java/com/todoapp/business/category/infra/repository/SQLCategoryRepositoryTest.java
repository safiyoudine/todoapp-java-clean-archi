package com.todoapp.business.category.infra.repository;

import com.todoapp.business.category.domain.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(SQLCategoryRepository.class)
class SQLCategoryRepositoryTest {

    @Autowired
    private SQLCategoryRepository categoryRepository;

    @Test
    public void testFindByLabel() {
        Category category = new Category();
        category.setLabel("Category 1");
        categoryRepository.save(category);

        Optional<Category> result = categoryRepository.findByLabel("Category 1");

        assertTrue(result.isPresent());
        assertEquals("Category 1", result.get().getLabel());
    }

    @Test
    public void testFindByLabelNotFound() {
        Optional<Category> result = categoryRepository.findByLabel("Nonexistent Category");

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindAll() {
        Category category1 = new Category();
        category1.setLabel("Category 1");
        Category category2 = new Category();
        category2.setLabel("Category 2");

        categoryRepository.save(category1);
        categoryRepository.save(category2);

        List<Category> categories = categoryRepository.findAll();

        assertEquals(2, categories.size());
        assertTrue(categories.stream().anyMatch(c -> c.getLabel().equals("Category 1")));
        assertTrue(categories.stream().anyMatch(c -> c.getLabel().equals("Category 2")));
    }
}