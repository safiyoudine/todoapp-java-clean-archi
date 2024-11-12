package com.todoapp.business.category.infra.controller;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.controller.request.CategoryRequest;
import com.todoapp.business.category.infra.controller.response.CategoryDto;
import com.todoapp.business.category.infra.mapper.CategoryMapper;
import com.todoapp.business.category.usecases.CreateCategory;
import com.todoapp.business.category.usecases.ListCategories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CreateCategory createCategory;

    @Mock
    private ListCategories listCategories;

    @InjectMocks
    private CategoryController categoryController;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        category = new Category();
        category.setLabel("java");

        categoryDto = new CategoryDto();
        categoryDto.setLabel("java");
    }

    @Test
    public void testGetCategories() throws Exception {

        List<Category> categories = new ArrayList<>();
        categories.add(category);

        when(listCategories.execute()).thenReturn(categories);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("java"));

        verify(listCategories).execute();
    }

    @Test
    public void testCreateTask_categoryCreated() throws Exception {

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setLabel("java");

        when(createCategory.execute(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/category")
                        .contentType("application/json")
                        .content("{\"label\":\"java\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.label").value("java"));

        verify(createCategory).execute(any(Category.class));
    }

    @Test
    public void testCreateTask_categoryAlreadyExists() throws Exception {

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setLabel("java");

        when(createCategory.execute(any(Category.class))).thenReturn(null);

        mockMvc.perform(post("/api/category")
                        .contentType("application/json")
                        .content("{\"label\":\"java\"}"))
                .andExpect(status().isBadRequest());

        verify(createCategory).execute(any(Category.class));
    }
}
