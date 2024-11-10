package com.todoapp.business.user.infra.mapper;


import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.domain.UserRole;
import com.todoapp.business.user.infra.controller.request.AuthRequest;
import com.todoapp.business.user.infra.controller.request.SignupRequest;
import com.todoapp.business.user.infra.controller.response.AuthDto;
import com.todoapp.business.user.infra.controller.response.UserDto;
import com.todoapp.business.user.infra.entity.UserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

    public static User toDomain(SignupRequest signupRequest) {
        User user = new User();
        user.setFirstName(user.getFirstName());
        user.setLastName(signupRequest.getLastname());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.USER);
        return user;
    }

    public static User toDomain(AuthRequest authRequest) {
        User user = new User(authRequest.getEmail(), authRequest.getPassword());
        return user;
    }

    // Mapper entre UserEntity et User (Domain)
    public static User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User user = new User(entity.getEmail(), entity.getPassword());
        user.setId(entity.getId());
        user.setLastName(entity.getLastname());
        user.setFirstName(entity.getFirstname());
        return user;
    }

    public static UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setLastname(user.getLastName());
        entity.setFirstname(user.getFirstName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        return entity;
    }

    // Mapper entre User (Domain) et UserDto
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setLastName(user.getLastName());
        dto.setFirstName(user.getFirstName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public static AuthDto toAuthDto(User user) {
        if (user == null) {
            return null;
        }
        AuthDto dto = new AuthDto();
        dto.setJwt(user.getToken());
        dto.setUserId(user.getId());
        dto.setUserRole(user.getUserRole().name());
        return dto;
    }


}