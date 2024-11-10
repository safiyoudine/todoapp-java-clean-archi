package com.todoapp.business.user.infra.service;


import com.todoapp.business.user.domain.User;

import java.util.Optional;

public interface UserService{

    Optional<User> getAuthenticatedUsername();

}
