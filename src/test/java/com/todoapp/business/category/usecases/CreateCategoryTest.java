package com.todoapp.business.category.usecases;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CreateCategory createCategory;

    private Category category;

    @BeforeEach
    public void setUp() {
        category = new Category();
        category.setLabel("java");
    }

    @Test
    public void testExecute_categoryNotExists() {
        when(categoryRepository.findByLabel(category.getLabel())).thenReturn(Optional.empty());
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = createCategory.execute(category);

        assertNotNull(result, "The category should be saved.");
        assertEquals(category.getLabel(), result.getLabel(), "The saved category label should match.");
        verify(categoryRepository).save(category);
    }

    @Test
    public void testExecute_categoryExists() {
        // Cas où la catégorie existe déjà, elle ne doit pas être sauvegardée et la méthode doit retourner null
        when(categoryRepository.findByLabel(category.getLabel())).thenReturn(Optional.of(category));

        Category result = createCategory.execute(category);

        assertNull(result, "The category should not be saved and the result should be null.");
        verify(categoryRepository, never()).save(category);
    }

    @Test
    public void testExecute_categoryExists_noSave() {
        // Cas où la catégorie existe déjà et on vérifie que save n'est pas appelé
        when(categoryRepository.findByLabel("java")).thenReturn(Optional.of(category));

        Category result = createCategory.execute(category);

        assertNull(result, "The category should not be saved if it already exists.");
        verify(categoryRepository, never()).save(category);
    }
}
