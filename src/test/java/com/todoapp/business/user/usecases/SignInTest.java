package com.todoapp.business.user.usecases;

import com.todoapp.business.user.domain.LoginPassword;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.repository.UserRepository;
import com.todoapp.business.user.infra.service.CustomUserDetails;
import com.todoapp.business.user.infra.service.CustomUserDetailsService;
import com.todoapp.commons.infra.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignInTest {

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SignIn signIn;

    private LoginPassword loginPassword;
    private CustomUserDetails userDetails;
    private User user;

    @BeforeEach
    public void setUp() {
        loginPassword = new LoginPassword("test@example.com", "validPassword");

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("validPassword");
        user.setToken("");

        userDetails = new CustomUserDetails(user, List.of());
    }

    @Test
    public void testExecute_success() {

        when(customUserDetailsService.loadUserByUsername(loginPassword.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginPassword.getPassword(), userDetails.getPassword())).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("validToken");

        User result = signIn.execute(loginPassword);

        assertNotNull(result, "User should not be null");
        assertEquals("validToken", result.getToken(), "The token should be assigned to the user");
        verify(userRepository).save(result);
    }

    @Test
    public void testExecute_badPassword() {

        when(customUserDetailsService.loadUserByUsername(loginPassword.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches(loginPassword.getPassword(), userDetails.getPassword())).thenReturn(false);

        BadCredentialsException thrown = assertThrows(BadCredentialsException.class, () -> signIn.execute(loginPassword));

        assertEquals("Invalid username or password", thrown.getMessage(), "The exception message should indicate invalid credentials");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testExecute_userNotFound() {
        when(customUserDetailsService.loadUserByUsername(loginPassword.getEmail())).thenReturn(null);

        BadCredentialsException thrown = assertThrows(BadCredentialsException.class, () -> signIn.execute(loginPassword));

        assertEquals("Invalid username or password", thrown.getMessage(), "The exception message should indicate invalid credentials");
        verify(userRepository, never()).save(any(User.class));
    }
}
