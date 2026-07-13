package com.inno.business.auth.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;

public interface UserRepositoryPort {

    Optional<User> findByEmail(String email);
    Optional<User> findById(UUID id);    
    boolean existsByEmail(String email);
    User save(User user);
}
