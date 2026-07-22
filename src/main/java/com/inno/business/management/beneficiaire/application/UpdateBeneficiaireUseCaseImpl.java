package com.inno.business.management.beneficiaire.application;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.beneficiaire.domain.exception.BeneficiaireNotFoundException;
import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.model.StatutBeneficiaire;
import com.inno.business.management.beneficiaire.domain.model.TypeCourse;
import com.inno.business.management.beneficiaire.domain.port.in.UpdateBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;

public class UpdateBeneficiaireUseCaseImpl implements UpdateBeneficiaireUseCase {
    private final UserRepositoryPort         userRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;
    private final SpringCompanyRepository    companyRepository;

    public UpdateBeneficiaireUseCaseImpl(UserRepositoryPort userRepository,
                                         BeneficiaireRepositoryPort beneficiaireRepository,
                                         SpringCompanyRepository companyRepository) {
        this.userRepository         = userRepository;
        this.beneficiaireRepository = beneficiaireRepository;
        this.companyRepository      = companyRepository;
    }
      @Override
    public Beneficiaire execute(UpdateBeneficiaireCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Beneficiaire existing = beneficiaireRepository.findById(cmd.beneficiaireId())
                .orElseThrow(() -> new BeneficiaireNotFoundException(cmd.beneficiaireId().toString()));

        if (!companyRepository.existsByIdAndUserId(existing.getSocieteId(), owner.getId()))
            throw new UnauthorizedAccessException();

        StatutBeneficiaire statut = cmd.statut() != null
                ? StatutBeneficiaire.valueOf(cmd.statut().toUpperCase())
                : existing.getStatut();

        Beneficiaire updated = new Beneficiaire(
                existing.getId(),
                existing.getSiteId(),
                existing.getSocieteId(),
                cmd.prenom() != null ? cmd.prenom() : existing.getPrenom(),
                cmd.nom() != null ? cmd.nom() : existing.getNom(),
                cmd.email() != null ? cmd.email() : existing.getEmail(),
                cmd.telephone() != null ? cmd.telephone() : existing.getTelephone(),
                cmd.cin() != null ? cmd.cin() : existing.getCin(),
                cmd.matricule() != null ? cmd.matricule() : existing.getMatricule(),
                cmd.adresses() != null ? cmd.adresses() : existing.getAdresses(),
                TypeCourse.fromString(cmd.typeCourse()),
                statut,
                existing.getActiviteId(),
                existing.getShiftId(),
                existing.getCreatedAt()
        );

        return beneficiaireRepository.save(updated);
    }



}
