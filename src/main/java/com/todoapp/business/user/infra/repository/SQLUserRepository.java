package com.todoapp.business.user.infra.repository;

import com.todoapp.business.user.domain.User;
import com.todoapp.business.user.infra.entity.UserEntity;
import com.todoapp.business.user.infra.mapper.UserMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class SQLUserRepository implements UserRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<User> findByUsername(String username) {
        return findByEmail(username);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT u FROM UserEntity u WHERE u.email = :email";  // Correction ici
        try {
            UserEntity userEntity = entityManager.createQuery(sql, UserEntity.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(UserMapper.toDomain(userEntity));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public User save(User user) {
        UserEntity userEntity = UserMapper.toEntity(user);
        if(userEntity.getId() == null) {
            entityManager.persist(userEntity);
        } else {
            entityManager.merge(userEntity);
        }
        return UserMapper.toDomain(userEntity);
    }
}
