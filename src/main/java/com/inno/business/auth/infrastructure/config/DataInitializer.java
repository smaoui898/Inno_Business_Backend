package com.inno.business.auth.infrastructure.config;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inno.business.auth.domain.model.TypeSociete;
import com.inno.business.auth.infrastructure.adapter.out.persistence.CompanyJpaEntity;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.UserJpaEntity;

@Component
public class DataInitializer implements CommandLineRunner {

    private final SpringUserRepository    userRepo;
    private final SpringCompanyRepository companyRepo;
    private final PasswordEncoder         encoder;

    public DataInitializer(SpringUserRepository userRepo,
                           SpringCompanyRepository companyRepo,
                           PasswordEncoder encoder) {
        this.userRepo    = userRepo;
        this.companyRepo = companyRepo;
        this.encoder     = encoder;
    }

    @Override
    public void run(String... args) {
        if (userRepo.findByEmail("admin@inno.tn").isEmpty()) {

            UserJpaEntity admin = new UserJpaEntity();
            admin.setPrenom("Admin");
            admin.setNom("InnoBusiness");
            admin.setTelephone("+21658000800");
            admin.setEmail("admin@inno.tn");
            admin.setPassword(encoder.encode("password123"));
            admin.setRole("ROLE_ADMIN");
            admin.setCreatedAt(LocalDateTime.now());
            UserJpaEntity savedAdmin = userRepo.save(admin);

            CompanyJpaEntity company = new CompanyJpaEntity();
            company.setUser(savedAdmin);
            company.setTypeSociete(TypeSociete.BUSINESS);
            company.setNomGroupe("Inno Group");
            company.setRaisonSociale("InnoBusiness SARL");
            company.setIdentifiantUnique("INN-001");
            company.setAdresse("Rue de la République");
            company.setVille("Sfax");
            company.setEmailSociete("contact@inno.tn");
            company.setTelephoneSociete("+21674000000");
            company.setRneDocumentPath("N/A");
            company.setPatenteDocumentPath("N/A");
            company.setCinGerantDocumentPath("N/A");
            company.setCreatedAt(LocalDateTime.now());
            companyRepo.save(company);

            System.out.println(" Admin créé     : admin@inno.tn / password123");
            System.out.println(" Company créée  : Inno Group (BUSINESS)");
        }
    }
}