package com.inno.inno_project.auth.infrastructure.adapter.out.persistence;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.inno.inno_project.auth.domain.model.User;
import com.inno.inno_project.auth.domain.port.out.UserRepositoryPort;
@Component
public class UserRepositoryAdapter implements UserRepositoryPort { //le vrai adaptateur

    private final SpringUserRepository springUserRepository;

    public UserRepositoryAdapter(SpringUserRepository springUserRepository) {
        this.springUserRepository = springUserRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return springUserRepository.findByEmail(email).map(UserJpaEntity::toDomain);
    }

    @Override
    public User save(User user) {
        return springUserRepository.save(UserJpaEntity.fromDomain(user)).toDomain();
    }

    @Override
    public boolean existsByEmail(String email) {
        return springUserRepository.existsByEmail(email);
    }

}
