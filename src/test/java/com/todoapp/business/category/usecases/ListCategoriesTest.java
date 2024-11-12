package com.todoapp.business.category.usecases;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCategoriesTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ListCategories listCategories;

    private Category category1;
    private Category category2;

    @BeforeEach
    public void setUp() {

        category1 = new Category();
        category1.setLabel("java");

        category2 = new Category();
        category2.setLabel("python");
    }

    @Test
    public void testExecute_withCategories() {
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = listCategories.execute();

        assertNotNull(result, "The result should not be null.");
        assertEquals(2, result.size(), "The size of the result list should match the expected number of categories.");
        assertTrue(result.contains(category1), "The result should contain category1.");
        assertTrue(result.contains(category2), "The result should contain category2.");
    }

    @Test
    public void testExecute_noCategories() {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());

        List<Category> result = listCategories.execute();

        assertNotNull(result, "The result should not be null.");
        assertTrue(result.isEmpty(), "The result list should be empty.");
    }
}
