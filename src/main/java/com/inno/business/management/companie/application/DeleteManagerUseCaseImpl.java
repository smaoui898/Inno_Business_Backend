package com.inno.business.management.companie.application;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.DeleteManagerUseCase;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

import java.util.List;
import java.util.UUID;

public class DeleteManagerUseCaseImpl implements DeleteManagerUseCase {

    private final UserRepositoryPort    userRepository;
    private final ManagerRepositoryPort managerRepository;
    private final SocieteRepositoryPort societeRepository;

    public DeleteManagerUseCaseImpl(UserRepositoryPort userRepository,
                                    ManagerRepositoryPort managerRepository,
                                    SocieteRepositoryPort societeRepository) {
        this.userRepository    = userRepository;
        this.managerRepository = managerRepository;
        this.societeRepository = societeRepository;
    }

    @Override
    public void execute(String ownerEmail, UUID managerId) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner non trouvé"));

        User manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager non trouvé : " + managerId));

        // Vérifie que ce manager appartient à cet owner
        if (!owner.getId().equals(manager.getCreatedByUserId()))
            throw new UnauthorizedAccessException();

        // 1. Désassigner toutes les sociétés de ce manager
        List<Societe> societes = societeRepository.findAllByManagerId(managerId);
        for (Societe s : societes) {
            Societe updated = new Societe(
                    s.getId(), s.getOwnerId(), s.getSocieteCode(),
                    s.getTypeSociete(), s.getRaisonSociale(), s.getIdentifiantUnique(),
                    s.getAdresse(), s.getVille(), s.getEmailSociete(), s.getTelephoneSociete(),
                    s.getRneDocumentPath(), s.getPatenteDocumentPath(), s.getCinGerantDocumentPath(),
                    null,         // managerId = null
                    s.isActive(), s.getCreatedAt()
            );
            societeRepository.save(updated);
        }

        // 2. Supprimer l'utilisateur manager
        managerRepository.deleteManager(managerId);
    }
}