package com.todoapp.business.user.usecases;

import com.todoapp.business.user.domain.LoginPassword;
import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.repository.UserRepository;
import com.todoapp.business.user.infra.service.CustomUserDetails;
import com.todoapp.business.user.infra.service.CustomUserDetailsService;
import com.todoapp.commons.infra.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SignIn {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    public User execute(LoginPassword loginPassword) {

        Authentication authentication = authenticate(loginPassword.getEmail(), loginPassword.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String userRole = "";
        for (GrantedAuthority authority : authorities) {
            userRole = authority.getAuthority();
        }

        User user = userDetails.getUser();
        user.setToken(token);
        userRepository.save(user);

        return user;
    }

    private Authentication authenticate(String username, String password) {
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

}
