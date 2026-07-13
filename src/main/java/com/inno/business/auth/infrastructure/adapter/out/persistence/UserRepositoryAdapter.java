package com.inno.business.auth.infrastructure.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
@Component
public class UserRepositoryAdapter implements UserRepositoryPort { //le vrai adaptateur

    private final SpringUserRepository springUserRepository;

    public UserRepositoryAdapter(SpringUserRepository springUserRepository) {
        this.springUserRepository = springUserRepository;
    }
    // trouver un user par son id pour un beneficiaire
    @Override
    public Optional<User> findById(UUID id) {        
        return springUserRepository.findById(id).map(UserJpaEntity::toDomain);
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
