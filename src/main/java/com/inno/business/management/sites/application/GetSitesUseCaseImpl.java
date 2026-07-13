package com.inno.business.management.sites.application;

import java.util.List;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.sites.domain.model.Site;
import com.inno.business.management.sites.domain.port.in.GetSitesUseCase;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;

public class GetSitesUseCaseImpl implements GetSitesUseCase {

    private final UserRepositoryPort       userRepository;
    private final SiteRepositoryPort       siteRepository;
    private final SpringCompanyRepository  companyRepository;

    public GetSitesUseCaseImpl(UserRepositoryPort userRepository,
                               SiteRepositoryPort siteRepository,
                               SpringCompanyRepository companyRepository) {
        this.userRepository    = userRepository;
        this.siteRepository    = siteRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Site> execute(String ownerEmail, UUID societeId) {
        // recuperer le user par son email
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifie que la société appartient au user
        if (!companyRepository.existsByIdAndUserId(societeId, owner.getId()))
            throw new UnauthorizedAccessException();
        //trouver tous les sites de la societe
        return siteRepository.findAllBySocieteId(societeId);
    }
}

