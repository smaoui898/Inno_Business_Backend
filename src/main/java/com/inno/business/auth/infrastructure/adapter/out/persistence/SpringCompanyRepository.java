package com.inno.business.auth.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringCompanyRepository extends JpaRepository<CompanyJpaEntity, UUID> {
   //pour auth 
    Optional<CompanyJpaEntity> findByUserId(UUID userId);
    //pour societe 
    List<CompanyJpaEntity> findAllByUserId(UUID userId);
    List<CompanyJpaEntity> findAllByManagerId(UUID managerId);
    boolean existsByIdAndUserId(UUID id, UUID userId);
}
