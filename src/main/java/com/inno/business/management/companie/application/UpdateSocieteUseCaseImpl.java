package com.inno.business.management.companie.application;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.exception.SocieteNotFoundException;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.UpdateSocieteUseCase;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

public class UpdateSocieteUseCaseImpl implements UpdateSocieteUseCase {

    private final UserRepositoryPort    userRepository;
    private final SocieteRepositoryPort societeRepository;

    public UpdateSocieteUseCaseImpl(UserRepositoryPort userRepository,
                                    SocieteRepositoryPort societeRepository) {
        this.userRepository    = userRepository;
        this.societeRepository = societeRepository;
    }


    @Override
    public Societe execute(UpdateSocieteCommand cmd) {
        // 1. Récupération de l'owner via son email
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Vérification que la société appartient à cet owner
        if (!societeRepository.existsByIdAndOwnerId(cmd.societeId(), owner.getId()))
            throw new UnauthorizedAccessException();

        Societe existing = societeRepository.findById(cmd.societeId())
                .orElseThrow(() -> new SocieteNotFoundException(cmd.societeId().toString()));

        // Création d'une nouvelle instance avec les champs mis à jour
        Societe updated = new Societe(
                existing.getId(),
                existing.getOwnerId(),
                existing.getSocieteCode(),
                existing.getTypeSociete(),
                cmd.raisonSociale() != null ? cmd.raisonSociale() : existing.getRaisonSociale(),
                cmd.identifiantUnique() != null ? cmd.identifiantUnique() : existing.getIdentifiantUnique(),
                cmd.adresse() != null ? cmd.adresse() : existing.getAdresse(),
                cmd.ville() != null ? cmd.ville() : existing.getVille(),
                cmd.emailSociete() != null ? cmd.emailSociete() : existing.getEmailSociete(),
                cmd.telephoneSociete() != null ? cmd.telephoneSociete() : existing.getTelephoneSociete(),
                existing.getRneDocumentPath(),
                existing.getPatenteDocumentPath(),
                existing.getCinGerantDocumentPath(),
                existing.getManagerId(),
                existing.isActive(),
                existing.getCreatedAt()
        );

        return societeRepository.save(updated); // teb3a adapter
    }
}