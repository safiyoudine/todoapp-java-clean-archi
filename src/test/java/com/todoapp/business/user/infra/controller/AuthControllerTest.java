package com.todoapp.business.user.infra.controller;

import com.todoapp.business.user.domain.LoginPassword;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.domain.UserRole;
import com.todoapp.business.user.infra.controller.request.AuthRequest;
import com.todoapp.business.user.infra.controller.request.SignupRequest;
import com.todoapp.business.user.infra.controller.response.AuthDto;
import com.todoapp.business.user.infra.controller.response.UserDto;
import com.todoapp.business.user.usecases.SignIn;
import com.todoapp.business.user.usecases.SignUp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SignUp signUp;

    @Mock
    private SignIn signIn;

    @InjectMocks
    private AuthController authController;

    private User user;
    private UserDto userDto;
    private SignupRequest signupRequest;
    private AuthDto authDto;
    private AuthRequest authRequest;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        user = new User();
        user.setId(1L);
        user.setEmail("testUser");
        user.setPassword("password");
        user.setUserRole(UserRole.USER);
        user.setToken("sampleAuthToken");

        userDto = new UserDto();
        userDto.setEmail("testUser");

        signupRequest = new SignupRequest();
        signupRequest.setEmail("testUser");
        signupRequest.setPassword("password");

        authDto = new AuthDto();
        authDto.setJwt("sampleAuthToken");

        authRequest = new AuthRequest();
        authRequest.setEmail("testUser");
        authRequest.setPassword("password");
    }

    @Test
    public void testSignupUser_success() throws Exception {
        when(signUp.execute(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content("{\"email\":\"testUser\", \"password\":\"password\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("testUser"));

        verify(signUp).execute(any(User.class));
    }

    @Test
    public void testSignupUser_failure() throws Exception {
        when(signUp.execute(any(User.class))).thenReturn(null);

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content("{\"username\":\"testUser\", \"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User Not Created!"));

        verify(signUp).execute(any(User.class));
    }

    @Test
    public void testSignin_success() throws Exception {
        when(signIn.execute(any(LoginPassword.class))).thenReturn(user);

        mockMvc.perform(post("/auth/signin")
                        .contentType("application/json")
                        .content("{\"email\":\"testUser\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").value("sampleAuthToken"));

        verify(signIn).execute(any(LoginPassword.class));
    }
}
