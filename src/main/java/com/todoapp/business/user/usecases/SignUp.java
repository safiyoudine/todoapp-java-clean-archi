package com.todoapp.business.user.usecases;

import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SignUp {

    @Autowired
    private UserRepository userRepository;

    public User execute(User user) {
        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
        if (!userOptional.isPresent()) {
            return userRepository.save(user);
        }
        return null;
    }
}
