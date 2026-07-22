package com.inno.business.management.sites.application;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.sites.domain.exception.SiteNotFoundException;
import com.inno.business.management.sites.domain.model.Site;
import com.inno.business.management.sites.domain.port.in.UpdateSiteUseCase;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;

public class UpdateSiteUseCaseImpl implements UpdateSiteUseCase{
     private final UserRepositoryPort      userRepository;
    private final SiteRepositoryPort      siteRepository;
    private final SpringCompanyRepository companyRepository;
     public UpdateSiteUseCaseImpl(UserRepositoryPort userRepository,
                                 SiteRepositoryPort siteRepository,
                                 SpringCompanyRepository companyRepository) {
        this.userRepository    = userRepository;
        this.siteRepository    = siteRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Site execute(UpdateSiteCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Site existing = siteRepository.findById(cmd.siteId())
                .orElseThrow(() -> new SiteNotFoundException(cmd.siteId().toString()));

        // Vérifie que la société du site appartient à cet owner
        if (!companyRepository.existsByIdAndUserId(existing.getSocieteId(), owner.getId()))
            throw new UnauthorizedAccessException();

        Site updated = new Site(
                existing.getId(),
                existing.getSocieteId(),
                cmd.nom() != null ? cmd.nom() : existing.getNom(),
                cmd.adresse() != null ? cmd.adresse() : existing.getAdresse(),
                cmd.ville() != null ? cmd.ville() : existing.getVille(),
                cmd.telephone() != null ? cmd.telephone() : existing.getTelephone(),
                cmd.email() != null ? cmd.email() : existing.getEmail(),
                existing.getResponsableId(),
                existing.getCreatedAt()
        );

        return siteRepository.save(updated);
    }


}
