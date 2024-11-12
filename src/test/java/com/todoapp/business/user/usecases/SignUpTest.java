package com.todoapp.business.user.usecases;


import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.repository.UserRepository;
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
class SignUpTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SignUp signUp;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
    }

    @Test
    public void testExecute_userNotExist() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User result = signUp.execute(user);

        assertNotNull(result, "The user should be saved.");
        assertEquals(user.getEmail(), result.getEmail(), "The email of the created user should match.");
        verify(userRepository).save(user);
    }

    @Test
    public void testExecute_userAlreadyExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        User result = signUp.execute(user);

        assertNull(result, "The user should not be created if it already exists.");
        verify(userRepository, never()).save(user);
    }
}