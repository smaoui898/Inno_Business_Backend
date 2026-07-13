package com.inno.business.management.companie.application;

import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.companie.domain.ports.in.DeleteSocieteUseCase;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

public class DeleteSocieteUseCaseImpl implements DeleteSocieteUseCase {

    private final UserRepositoryPort userRepository;
    private final SocieteRepositoryPort societeRepository;

    public DeleteSocieteUseCaseImpl(UserRepositoryPort userRepository,
            SocieteRepositoryPort societeRepository) {
        this.userRepository = userRepository;
        this.societeRepository = societeRepository;
    }

    @Override
    public void execute(String ownerEmail, UUID societeId) {
        // 1. Récupération de l'owner
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // 2. Vérification d'appartenance
        if (!societeRepository.existsByIdAndOwnerId(societeId, owner.getId())) {
            throw new UnauthorizedAccessException();
        }

        // 3. Suppression
        societeRepository.delete(societeId); // teb3a adapter
    }
}

