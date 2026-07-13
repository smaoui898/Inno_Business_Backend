package com.inno.business.auth.infrastructure.adapter.out.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.inno.business.auth.domain.model.Company;
import com.inno.business.auth.domain.port.out.CompanyRepositoryPort;

@Component
public class CompanyRepositoryAdapter implements CompanyRepositoryPort {

    private final SpringCompanyRepository companyRepo;
    private final SpringUserRepository    userRepo;

    public CompanyRepositoryAdapter(SpringCompanyRepository companyRepo,
            SpringUserRepository userRepo) {
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
    }

    @Override
    public Company save(Company company) {
        // Résolution de la relation JPA : on charge UserJpaEntity par userId
        UserJpaEntity userEntity = userRepo.findById(company.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        "Utilisateur introuvable pour la société : " + company.getUserId()));

        CompanyJpaEntity entity = CompanyJpaEntity.fromDomain(company, userEntity);
        return companyRepo.save(entity).toDomain();
    }

    @Override
    public Optional<Company> findByUserId(UUID userId) {
        return companyRepo.findByUserId(userId).map(CompanyJpaEntity::toDomain);
    }
}