package com.inno.business.management.companie.application;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.PasswordEncoderPort;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.domain.valueobject.Email;
import com.inno.business.management.companie.domain.exception.SocieteNotFoundException;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.CreateManagerUseCase;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

public class CreateManagerUseCaseImpl implements CreateManagerUseCase {

    private final UserRepositoryPort userRepository;
    private final ManagerRepositoryPort managerRepository;
    private final SocieteRepositoryPort societeRepository;
    private final PasswordEncoderPort passwordEncoder;

    public CreateManagerUseCaseImpl(UserRepositoryPort userRepository, PasswordEncoderPort passwordEncoder, SocieteRepositoryPort societeRepository, ManagerRepositoryPort managerRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.societeRepository = societeRepository;
        this.managerRepository = managerRepository;
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public CreateManagerResult execute(CreateManagerCommand cmd) {
        // 1. Vérifier que l'owner existe et appartient à la société
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Owner non trouvé"));

        if (managerRepository.existsByEmail(cmd.email())) {
            throw new RuntimeException("Un compte existe déjà avec cet email : " + cmd.email());
        }

        // 2. Créer le manager
        User manager = new User(
                null, // null → @GeneratedValue génère l'UUID via persist()
                cmd.prenom(),
                cmd.nom(),
                cmd.telephone(),
                new Email(cmd.email()),
                passwordEncoder.encode(cmd.password()),
                "ROLE_MANAGER",
                LocalDateTime.now(),
                owner.getId() // createdByUserId
        );
        
        // 3. Sauvegarder le manager
        User savedManager = managerRepository.createManager(manager); // teb3a adapter

        // Assignation aux sociétés sélectionnées
        for (UUID societeId : cmd.societeIds()) {
            if (!societeRepository.existsByIdAndOwnerId(societeId, owner.getId())) {
                throw new UnauthorizedAccessException();
            }

            Societe societe = societeRepository.findById(societeId)
                    .orElseThrow(() -> new SocieteNotFoundException(societeId.toString()));

            Societe updated = new Societe(
                    societe.getId(), societe.getOwnerId(), societe.getSocieteCode(),
                    societe.getTypeSociete(), societe.getRaisonSociale(),
                    societe.getIdentifiantUnique(), societe.getAdresse(), societe.getVille(),
                    societe.getEmailSociete(), societe.getTelephoneSociete(),
                    societe.getRneDocumentPath(), societe.getPatenteDocumentPath(),
                    societe.getCinGerantDocumentPath(),
                    savedManager.getId(), // ← assign manager
                    societe.isActive(), societe.getCreatedAt()
            );
            societeRepository.save(updated);
        }

        return new CreateManagerResult(
                savedManager.getId(),
                savedManager.getEmailValue(),
                savedManager.getPrenom(),
                savedManager.getNom()
        );
    }
}
