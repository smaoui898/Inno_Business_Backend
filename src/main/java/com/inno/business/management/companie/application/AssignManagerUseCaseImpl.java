package com.inno.business.management.companie.application;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.exception.SocieteNotFoundException;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.AssignManagerUseCase;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

public class AssignManagerUseCaseImpl implements AssignManagerUseCase {

    private final UserRepositoryPort    userRepository;
    private final SocieteRepositoryPort societeRepository;

    public AssignManagerUseCaseImpl(UserRepositoryPort userRepository,
                                    SocieteRepositoryPort societeRepository) {
        this.userRepository    = userRepository;
        this.societeRepository = societeRepository;
    }

    @Override
    public void execute(AssignManagerCommand cmd) {
        // 1. Vérifier que l'owner existe
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé : " + cmd.ownerEmail()));

        // 2. Vérifier que la société existe d'abord
        Societe societe = societeRepository.findById(cmd.societeId())
                .orElseThrow(() -> new RuntimeException("Société non trouvée : " + cmd.societeId()));

        // 3. Vérifier que la société appartient à cet owner
        // On accepte aussi si ownerId est null (données legacy)
        if (societe.getOwnerId() != null && !societe.getOwnerId().equals(owner.getId())) {
            throw new UnauthorizedAccessException();
        }

        // 4. Créer une nouvelle instance avec le manager mis à jour
        Societe updated = new Societe(
                societe.getId(), societe.getOwnerId(), societe.getSocieteCode(),
                societe.getTypeSociete(), societe.getRaisonSociale(),
                societe.getIdentifiantUnique(), societe.getAdresse(), societe.getVille(),
                societe.getEmailSociete(), societe.getTelephoneSociete(),
                societe.getRneDocumentPath(), societe.getPatenteDocumentPath(),
                societe.getCinGerantDocumentPath(),
                cmd.managerId(),    // null = désassigner
                societe.isActive(), societe.getCreatedAt()
        );

        societeRepository.save(updated);
    }
}