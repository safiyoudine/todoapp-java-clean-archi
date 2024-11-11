package com.todoapp.business.user.infra.controller;

import com.todoapp.business.user.domain.LoginPassword;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.controller.request.AuthRequest;
import com.todoapp.business.user.infra.controller.request.SignupRequest;
import com.todoapp.business.user.infra.controller.response.AuthDto;
import com.todoapp.business.user.infra.controller.response.UserDto;
import com.todoapp.business.user.infra.mapper.UserMapper;
import com.todoapp.business.user.usecases.SignIn;
import com.todoapp.business.user.usecases.SignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private SignUp signUp;

    @Autowired
    private SignIn signIn;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
        User user = UserMapper.toDomain(signupRequest);
        UserDto createdDto = UserMapper.toDto(signUp.execute(user));
        if (createdDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Not Created!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthDto> signin(@RequestBody AuthRequest authRequest) {
        LoginPassword loginPassword = UserMapper.toDomain(authRequest);
        AuthDto authDto = UserMapper.toAuthDto(signIn.execute(loginPassword));
        return new ResponseEntity<>(authDto, HttpStatus.OK);
    }

}
