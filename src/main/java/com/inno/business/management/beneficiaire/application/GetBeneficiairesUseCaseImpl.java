package com.inno.business.management.beneficiaire.application;

import java.util.List;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.beneficiaire.domain.port.in.GetBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;

public class GetBeneficiairesUseCaseImpl implements GetBeneficiairesUseCase {

    private final UserRepositoryPort userRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;
    private final SpringCompanyRepository companyRepository;

    public GetBeneficiairesUseCaseImpl(UserRepositoryPort userRepository,
            BeneficiaireRepositoryPort beneficiaireRepository,
            SpringCompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.beneficiaireRepository = beneficiaireRepository;
        this.companyRepository = companyRepository;
    }

    public PagedResult execute(String ownerEmail, BeneficiaireFilter filter) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Récupère la première societe du user (le contexte actif)
        // En production, le societeId actif serait dans le JWT ou passé en paramètre
        var companies = companyRepository.findAllByUserId(owner.getId());
        if (companies.isEmpty()) {
            return new PagedResult(List.of(), 0, 0, 0);
        }

        // Utilise la première société (le "basculer" gère le contexte actif)
        UUID societeId = companies.get(0).getId();

        return beneficiaireRepository.findAll(societeId, filter);
    }

}
