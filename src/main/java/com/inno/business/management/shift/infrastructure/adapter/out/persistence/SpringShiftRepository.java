package com.inno.business.management.shift.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringShiftRepository extends JpaRepository<ShiftJpaEntity, UUID> {

    List<ShiftJpaEntity> findAllByOwnerId(UUID ownerId);

    boolean existsByIdAndOwnerId(UUID id, UUID ownerId);
}

