package com.inno.business.auth.infrastructure.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

// JPA --> communication avec base de donnees
public interface SpringUserRepository extends JpaRepository<UserJpaEntity, UUID> {
    Optional<UserJpaEntity> findByEmail(String email);  
    boolean existsByEmail(String email);

}
