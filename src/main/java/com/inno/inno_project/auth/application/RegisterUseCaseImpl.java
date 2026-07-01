package com.inno.inno_project.auth.application;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.inno_project.auth.domain.exception.PasswordMismatchException;
import com.inno.inno_project.auth.domain.exception.UserAlreadyExistsException;
import com.inno.inno_project.auth.domain.model.User;
import com.inno.inno_project.auth.domain.port.in.RegisterUseCase;
import com.inno.inno_project.auth.domain.port.out.FileStoragePort;
import com.inno.inno_project.auth.domain.port.out.PasswordEncoderPort;
import com.inno.inno_project.auth.domain.port.out.UserRepositoryPort;
import com.inno.inno_project.auth.domain.valueobject.Email;

public class RegisterUseCaseImpl implements RegisterUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final FileStoragePort fileStorage;

    public RegisterUseCaseImpl(UserRepositoryPort userRepository,
                               PasswordEncoderPort passwordEncoder,
                               FileStoragePort fileStorage) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.fileStorage     = fileStorage;
    }

    @Override
    public RegisterResult execute(RegisterCommand cmd) {

        // 1. Vérification des mots de passe  //verif mdp
        if (!cmd.password().equals(cmd.confirmPassword()))
            throw new PasswordMismatchException();

        // 2. Vérification unicité email
        if (userRepository.existsByEmail(cmd.email()))
            throw new UserAlreadyExistsException(cmd.email());

        // 3. Stockage des documents
        String rnePath       = fileStorage.store(cmd.rneData(),cmd.rneFileName(),"rne");
        String patentePath   = fileStorage.store(cmd.patenteData(),cmd.patenteFileName(),"patente");
        String cinGerantPath = fileStorage.store(cmd.cinGerantData(),cmd.cinGerantFileName(),"cin");

        // 4. Hash du mot de passe
        String hashedPassword = passwordEncoder.encode(cmd.password());

        // 5. Création de l'utilisateur domaine
        User user = new User(
                UUID.randomUUID(),
                cmd.prenom(),
                cmd.nom(),
                cmd.telephone(),
                new Email(cmd.email()),
                hashedPassword,
                cmd.nomGroupe(),
                cmd.raisonSociale(),
                cmd.identifiantUnique(),
                cmd.adresse(),
                cmd.ville(),
                cmd.emailSociete(),
                cmd.telephoneSociete(),
                rnePath,
                patentePath,
                cinGerantPath,
                "ROLE_USER",
                LocalDateTime.now(),
                null             // lastLogin null à la création
        );

        userRepository.save(user);

        return new RegisterResult("Compte créé avec succès", user.getEmailValue()); //resultat
    }
}