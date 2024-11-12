package com.todoapp.business.task.infra.repository;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.entity.CategoryEntity;
import com.todoapp.business.task.domain.Task;
import com.todoapp.business.task.domain.TaskStatus;
import com.todoapp.business.task.infra.entity.TaskEntity;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.entity.UserEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(SQLTaskRepository.class)
class SQLTaskRepositoryTest {

    @Autowired
    private SQLTaskRepository taskRepository;

    @Autowired
    private EntityManager entityManager;

    private UserEntity userEntityInBase;
    private CategoryEntity categoryEntityInBase;
    private TaskEntity taskEntity;
    private TaskEntity taskEntity2;

    @BeforeEach
    public void setUp() {
        // Création des utilisateurs et catégories dans la base de données
        userEntityInBase = createUserEntity("saf", "saf", "test@test.com");
        categoryEntityInBase = createCategoryEntity("java");

        // Création des tâches
        taskEntity = createTaskEntity(userEntityInBase, categoryEntityInBase, "Task 1", "Task 1 description", TaskStatus.COMPLETED);
        taskEntity2 = createTaskEntity(userEntityInBase, categoryEntityInBase, "Task 2", "Task 2 description", TaskStatus.COMPLETED);
    }

    @Test
    public void testFindByTaskId() {
        Optional<Task> task = taskRepository.findByTaskId(taskEntity.getId(), taskEntity.getUserEntity().getId());
        assertTrue(task.isPresent(), "Task should be found by ID");
        assertEquals(taskEntity.getTitle(), task.get().getTitle(), "Task title should match");
    }

    @Test
    public void testFindByUserId() {
        Page<Task> tasksPage = taskRepository.findByUserId(taskEntity.getUserEntity().getId(), 0, 10);
        assertNotNull(tasksPage, "Tasks page should not be null");
        assertEquals(2, tasksPage.getContent().size(), "Should return two tasks");
        assertTaskTitlesContain(tasksPage, "Task 1", "Task 2");
    }

    @Test
    public void testSave() {
        User user = new User();
        user.setId(userEntityInBase.getId());
        Category category = new Category();
        category.setId(categoryEntityInBase.getId());

        Task task = new Task();
        task.setTitle("New Task");
        task.setDescription("New Task description");
        task.setTaskStatus(TaskStatus.COMPLETED);
        task.setUser(user);
        task.setCategory(category);

        Task savedTask = taskRepository.save(task);
        assertNotNull(savedTask.getId(), "Task ID should be generated");
        assertEquals("New Task", savedTask.getTitle(), "Task title should match");
    }

    @Test
    public void testDelete() {
        taskRepository.delete(taskEntity.getId());
        Optional<Task> deletedTask = taskRepository.findByTaskId(taskEntity.getId(), taskEntity.getUserEntity().getId());
        assertFalse(deletedTask.isPresent(), "Task should be deleted");
    }

    private void assertTaskTitlesContain(Page<Task> tasksPage, String... titles) {
        for (String title : titles) {
            assertTrue(tasksPage.getContent().stream().anyMatch(t -> t.getTitle().equals(title)),
                    "Task with title " + title + " should be present");
        }
    }

    private UserEntity createUserEntity(String firstName, String lastName, String email) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstname(firstName);
        userEntity.setLastname(lastName);
        userEntity.setEmail(email);
        entityManager.persist(userEntity);
        return userEntity;
    }

    private CategoryEntity createCategoryEntity(String label) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setLabel(label);
        entityManager.persist(categoryEntity);
        return categoryEntity;
    }

    private TaskEntity createTaskEntity(UserEntity userEntity, CategoryEntity categoryEntity, String title, String description, TaskStatus status) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(title);
        taskEntity.setDescription(description);
        taskEntity.setTaskStatus(status);
        taskEntity.setUserEntity(userEntity);
        taskEntity.setCategoryEntity(categoryEntity);
        entityManager.persist(taskEntity);
        return taskEntity;
    }
}