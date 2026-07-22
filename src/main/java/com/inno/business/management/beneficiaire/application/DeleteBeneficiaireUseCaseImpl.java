package com.inno.business.management.beneficiaire.application;

import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.beneficiaire.domain.exception.BeneficiaireNotFoundException;
import com.inno.business.management.beneficiaire.domain.port.in.DeleteBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;

public class DeleteBeneficiaireUseCaseImpl implements DeleteBeneficiaireUseCase {
     private final UserRepositoryPort         userRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;
    private final SpringCompanyRepository    companyRepository;

    public DeleteBeneficiaireUseCaseImpl(UserRepositoryPort userRepository,
                                         BeneficiaireRepositoryPort beneficiaireRepository,
                                         SpringCompanyRepository companyRepository) {
        this.userRepository         = userRepository;
        this.beneficiaireRepository = beneficiaireRepository;
        this.companyRepository      = companyRepository;
    }

    @Override
    public void execute(String ownerEmail, UUID beneficiaireId) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        var ben = beneficiaireRepository.findById(beneficiaireId)
                .orElseThrow(() -> new BeneficiaireNotFoundException(beneficiaireId.toString()));

        if (!companyRepository.existsByIdAndUserId(ben.getSocieteId(), owner.getId()))
            throw new UnauthorizedAccessException();

        beneficiaireRepository.delete(beneficiaireId);
    }

}
