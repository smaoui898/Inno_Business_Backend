package com.inno.business.management.companie.application;
import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.business.auth.domain.model.TypeSociete;
import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.FileStoragePort;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.CreateSocieteUseCase;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

public class CreateSocieteUseCaseImpl implements CreateSocieteUseCase{

    
    private final UserRepositoryPort    userRepository;
    private final SocieteRepositoryPort societeRepository;
    private final FileStoragePort       fileStorage;

    public CreateSocieteUseCaseImpl(UserRepositoryPort userRepository,
                                    SocieteRepositoryPort societeRepository,
                                    FileStoragePort fileStorage) {
        this.userRepository    = userRepository;
        this.societeRepository = societeRepository;
        this.fileStorage       = fileStorage;
    }

    @Override
    public Societe execute(CreateSocieteCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Stockage des documents
        String rnePath    = fileStorage.store(cmd.rneData(),     cmd.rneName(),     "rne");
        String patentePath= fileStorage.store(cmd.patenteData(), cmd.patenteName(), "patente");
        String cinPath    = fileStorage.store(cmd.cinData(),     cmd.cinName(),     "cin");

        Societe societe = new Societe(
                UUID.randomUUID(),
                owner.getId(),
                null,   // societeCode généré par JPA entity
                TypeSociete.fromString(cmd.typeSociete()),
                cmd.raisonSociale(),
                cmd.identifiantUnique(),
                cmd.adresse(),
                cmd.ville(),
                cmd.emailSociete(),
                cmd.telephoneSociete(),
                rnePath, patentePath, cinPath,
                null,   // pas de manager à la création
                true,   // active par défaut
                LocalDateTime.now()
        );

        return societeRepository.save(societe);
    }

}
