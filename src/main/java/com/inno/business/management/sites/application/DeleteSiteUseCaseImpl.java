package com.inno.business.management.sites.application;

import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.sites.domain.exception.SiteNotFoundException;
import com.inno.business.management.sites.domain.model.Site;
import com.inno.business.management.sites.domain.port.in.DeleteSiteUseCase;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;

public class DeleteSiteUseCaseImpl implements DeleteSiteUseCase {
    private final UserRepositoryPort      userRepository;
    private final SiteRepositoryPort      siteRepository;
    private final SpringCompanyRepository companyRepository;
     public DeleteSiteUseCaseImpl(UserRepositoryPort userRepository,
                                 SiteRepositoryPort siteRepository,
                                 SpringCompanyRepository companyRepository) {
        this.userRepository    = userRepository;
        this.siteRepository    = siteRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public void execute(String ownerEmail, UUID siteId) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new SiteNotFoundException(siteId.toString()));

        if (!companyRepository.existsByIdAndUserId(site.getSocieteId(), owner.getId()))
                throw new UnauthorizedAccessException();

        siteRepository.delete(siteId);
    }

}
