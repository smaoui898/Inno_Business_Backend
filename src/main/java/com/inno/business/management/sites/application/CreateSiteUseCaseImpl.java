package com.inno.business.management.sites.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.PasswordEncoderPort;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.domain.valueobject.Email;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.sites.domain.model.Site;
import com.inno.business.management.sites.domain.port.in.CreateSiteUseCase;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;

public class CreateSiteUseCaseImpl implements CreateSiteUseCase {
    private final UserRepositoryPort      userRepository;
    private final SiteRepositoryPort      siteRepository;
    private final PasswordEncoderPort     passwordEncoder;
    private final SpringCompanyRepository companyRepository;
    public CreateSiteUseCaseImpl(UserRepositoryPort userRepository,
                                 SiteRepositoryPort siteRepository,
                                 PasswordEncoderPort passwordEncoder,
                                 SpringCompanyRepository companyRepository) {
        this.userRepository    = userRepository;
        this.siteRepository    = siteRepository;
        this.passwordEncoder   = passwordEncoder;
        this.companyRepository = companyRepository;
    }
    @Override
    @Transactional
    public Site execute(CreateSiteCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!companyRepository.existsByIdAndUserId(cmd.societeId(), owner.getId()))
            throw new UnauthorizedAccessException();

        // Création du responsable si les infos sont fournies
        UUID responsableId = null;
        if (isResponsableProvided(cmd)) {
            if (userRepository.existsByEmail(cmd.responsableEmail()))
                throw new RuntimeException("Email du responsable déjà utilisé : " + cmd.responsableEmail());

            // ID généré ici côté domaine — null laissé pour laisser @GeneratedValue agir
            User responsable = new User(
                    null, // null → Hibernate fait persist() et génère l'UUID
                    cmd.responsablePrenom(),
                    cmd.responsableNom(),
                    cmd.responsableTelephone(),
                    new Email(cmd.responsableEmail()),
                    passwordEncoder.encode(cmd.responsablePassword()),
                    "ROLE_RESPONSABLE_SITE",
                    LocalDateTime.now(),
                    owner.getId()
            );
            User saved = userRepository.save(responsable);
            responsableId = saved.getId();
        }

        Site site = new Site(
                null, // null → Hibernate fait persist() et génère l'UUID via @GeneratedValue
                cmd.societeId(),
                cmd.nom(),
                cmd.adresse(),
                cmd.ville(),
                cmd.telephone(),
                cmd.email(),
                responsableId,
                LocalDateTime.now()
        );

        return siteRepository.save(site);
    }

    private boolean isResponsableProvided(CreateSiteCommand cmd) {
        return cmd.responsableEmail() != null && !cmd.responsableEmail().isBlank()
            && cmd.responsablePassword() != null && !cmd.responsablePassword().isBlank();
    }
    
    
}
