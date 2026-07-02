package com.inno.business.auth.domain.port.out;

import java.util.Optional;

import com.inno.business.auth.domain.model.User;

public interface UserRepositoryPort {

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User save(User user);
}
