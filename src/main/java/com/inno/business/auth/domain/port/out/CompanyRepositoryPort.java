package com.inno.business.auth.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.inno.business.auth.domain.model.Company;

public interface CompanyRepositoryPort {
    Company save(Company company);
    Optional<Company> findByUserId(UUID userId);
}
