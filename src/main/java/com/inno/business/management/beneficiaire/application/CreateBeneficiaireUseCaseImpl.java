package com.inno.business.management.beneficiaire.application;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.transaction.annotation.Transactional;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.model.StatutBeneficiaire;
import com.inno.business.management.beneficiaire.domain.model.TypeCourse;
import com.inno.business.management.beneficiaire.domain.port.in.CreateBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.sites.domain.exception.SiteNotFoundException;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;

public class CreateBeneficiaireUseCaseImpl implements CreateBeneficiaireUseCase {
    private final UserRepositoryPort         userRepository;
    private final SiteRepositoryPort         siteRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;
    private final SpringCompanyRepository    companyRepository;
    public CreateBeneficiaireUseCaseImpl(UserRepositoryPort userRepository,
                                         SiteRepositoryPort siteRepository,
                                         BeneficiaireRepositoryPort beneficiaireRepository,
                                         SpringCompanyRepository companyRepository) {
        this.userRepository         = userRepository;
        this.siteRepository         = siteRepository;
        this.beneficiaireRepository = beneficiaireRepository;
        this.companyRepository      = companyRepository;
    }
     @Override
    @Transactional
    public Beneficiaire execute(CreateBeneficiaireCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        var site = siteRepository.findById(cmd.siteId())
                .orElseThrow(() -> new SiteNotFoundException(cmd.siteId().toString()));

        // Vérifie que la société du site appartient à l'owner
        if (!companyRepository.existsByIdAndUserId(site.getSocieteId(), owner.getId()))
                throw new UnauthorizedAccessException();

        // Récupère la societeId depuis le site pour le stocker dans le bénéficiaire
        UUID societeId = site.getSocieteId();

        Beneficiaire beneficiaire = new Beneficiaire(
                null, // null → Hibernate fait persist() et génère l'UUID via @GeneratedValue
                cmd.siteId(),
                societeId,
                cmd.prenom(),
                cmd.nom(),
                cmd.email(),
                cmd.telephone(),
                cmd.cin(),
                cmd.matricule(),
                cmd.adresses(),
                TypeCourse.fromString(cmd.typeCourse()),
                StatutBeneficiaire.ACTIF,
                null, null,
                LocalDateTime.now()
        );

        return beneficiaireRepository.save(beneficiaire);
    }

}
