package com.inno.inno_project.auth.infrastructure.config;


import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inno.inno_project.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import com.inno.inno_project.auth.infrastructure.adapter.out.persistence.UserJpaEntity;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SpringUserRepository repo;
    private final PasswordEncoder encoder;

    public DataInitializer(SpringUserRepository repo, PasswordEncoder encoder) {
        this.repo    = repo;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (repo.findByEmail("admin@inno.tn").isEmpty()) {
            UserJpaEntity admin = new UserJpaEntity();
            admin.setPrenom("Admin");
            admin.setNom("InnoBusiness");
            admin.setTelephone("+21600000000");
            admin.setEmail("admin@inno.tn");
            admin.setPassword(encoder.encode("password123"));
            admin.setNomGroupe("Inno Group");
            admin.setRaisonSociale("InnoBusiness SARL");
            admin.setIdentifiantUnique("INN-001");
            admin.setAdresse("Rue de la République");
            admin.setVille("Sfax");
            admin.setEmailSociete("contact@inno.tn");
            admin.setTelephoneSociete("+21674000000");
            admin.setRneDocumentPath("N/A");
            admin.setPatenteDocumentPath("N/A");
            admin.setCinGerantDocumentPath("N/A");
            admin.setRole("ROLE_ADMIN");
            admin.setCreatedAt(LocalDateTime.now());
            repo.save(admin);
            System.out.println("Admin créé : admin@inno.tn / password123");
        }
    }
}