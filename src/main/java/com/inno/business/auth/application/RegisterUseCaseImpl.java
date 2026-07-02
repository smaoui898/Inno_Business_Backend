package com.inno.business.auth.application;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.business.auth.domain.exception.PasswordMismatchException;
import com.inno.business.auth.domain.exception.UserAlreadyExistsException;
import com.inno.business.auth.domain.model.Company;
import com.inno.business.auth.domain.model.TypeSociete;
import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.in.RegisterUseCase;
import com.inno.business.auth.domain.port.out.CompanyRepositoryPort;
import com.inno.business.auth.domain.port.out.FileStoragePort;
import com.inno.business.auth.domain.port.out.PasswordEncoderPort;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.domain.valueobject.Email;

public class RegisterUseCaseImpl implements RegisterUseCase {

    private final UserRepositoryPort userRepository;
    private final CompanyRepositoryPort companyRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final FileStoragePort fileStorage;

    public RegisterUseCaseImpl(UserRepositoryPort userRepository,
                                CompanyRepositoryPort companyRepository,
                                PasswordEncoderPort passwordEncoder,
                                FileStoragePort fileStorage) {
        this.userRepository  = userRepository;
        this.companyRepository = companyRepository;
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

        // 5. Création de l'utilisateur domaine
        User user = new User(
                null,
                cmd.prenom(),
                cmd.nom(),
                cmd.telephone(),
                new Email(cmd.email()),
                passwordEncoder.encode(cmd.password()),
                "ROLE_USER",
                LocalDateTime.now(),
                null
        );
        User savedUser = userRepository.save(user);

        Company company = new Company(
                null,
                savedUser.getId(),                         // FK vers users
                TypeSociete.fromString(cmd.typeSociete()),
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
                LocalDateTime.now()
        );
        companyRepository.save(company);
        return new RegisterResult("Compte créé avec succès", user.getEmailValue()); //resultat
    }
}