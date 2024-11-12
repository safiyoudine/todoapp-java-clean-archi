package com.todoapp.business.user.infra.repository;

import com.todoapp.business.category.domain.Category;
import com.todoapp.business.category.infra.repository.SQLCategoryRepository;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(SQLUserRepository.class)
class SQLUserRepositoryTest {

    @Autowired
    private SQLUserRepository userRepository;

    @Test
    public void testFindByLabel() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setLastName("Category 1");
        user.setFirstName("Category 1");
        user.setUserRole(UserRole.USER);
        userRepository.save(user);

        Optional<User> result = userRepository.findByEmail("admin@gmail.com");

        assertTrue(result.isPresent());
        assertEquals("Category 1", result.get().getFirstName());
    }


}