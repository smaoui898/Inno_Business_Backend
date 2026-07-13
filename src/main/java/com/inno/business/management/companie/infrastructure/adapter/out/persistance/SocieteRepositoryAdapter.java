package com.inno.business.management.companie.infrastructure.adapter.out.persistance;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.inno.business.auth.infrastructure.adapter.out.persistence.CompanyJpaEntity;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

@Component
public class SocieteRepositoryAdapter implements SocieteRepositoryPort {

    private final SpringCompanyRepository companyRepo;
    private final SpringUserRepository userRepo;

    public SocieteRepositoryAdapter(
            SpringCompanyRepository companyRepo,
            SpringUserRepository userRepo) {
        this.companyRepo = companyRepo;
        this.userRepo = userRepo;
    }

    @Override
    // find all societes of user with user id
    public List<Societe> findAllByOwnerId(UUID ownerId) {
        return companyRepo.findAllByUserId(ownerId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<Societe> findAllByManagerId(UUID managerId) {
        return companyRepo.findAllByManagerId(managerId)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    // find by id
    public Optional<Societe> findById(UUID id) {
        return companyRepo.findById(id)
                .map(this::toDomain);
    }

    @Override
    @Transactional
    // save societe
    public Societe save(Societe societe) {
        // getReferenceById crée juste un proxy JPA (pas de SELECT), 
        // ce qui évite les entités détachées dans la même transaction
        UserJpaEntity ownerRef = userRepo.getReferenceById(societe.getOwnerId());

        CompanyJpaEntity entity;
        if (societe.getId() != null) {
            entity = companyRepo.findById(societe.getId())
                    .orElse(new CompanyJpaEntity());
        } else {
            entity = new CompanyJpaEntity();
        }

        entity.setUser(ownerRef);
        entity.setTypeSociete(societe.getTypeSociete());
        entity.setRaisonSociale(societe.getRaisonSociale());
        if (entity.getNomGroupe() == null) {
            entity.setNomGroupe(societe.getRaisonSociale()); // Fallback pour respecter le NOT NULL
        }
        entity.setIdentifiantUnique(societe.getIdentifiantUnique());
        entity.setAdresse(societe.getAdresse());
        entity.setVille(societe.getVille());
        entity.setEmailSociete(societe.getEmailSociete());
        entity.setTelephoneSociete(societe.getTelephoneSociete());
        entity.setRneDocumentPath(societe.getRneDocumentPath());
        entity.setPatenteDocumentPath(societe.getPatenteDocumentPath());
        entity.setCinGerantDocumentPath(societe.getCinGerantDocumentPath());
        entity.setManagerId(societe.getManagerId());
        entity.setActive(societe.isActive());

        // Génère le code si absent
        if (entity.getSocieteCode() == null) {
            entity.setSocieteCode(String.valueOf(10000 + new Random().nextInt(90000)));
        }
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(societe.getCreatedAt());
        }

        CompanyJpaEntity saved = companyRepo.save(entity);
        // Forcer le chargement du user dans la même transaction pour toDomain()
        saved.getUser().getId();
        return toDomain(saved);
    }

    @Override
    public void delete(UUID id) {
        companyRepo.deleteById(id);
    }

    @Override
    // exists by id and owner id
    public boolean existsByIdAndOwnerId(UUID id, UUID ownerId) {
        return companyRepo.existsByIdAndUserId(id, ownerId);
    }

    // convert to domain entity // prend en paramètre un objet de CompanyJpaEntity et extrait toutes les informations pour créer un objet de Societe
    // convertir domaine lel métier
    // adaptateur utilise toDomain() pour transformer l'entité en Societe
    private Societe toDomain(CompanyJpaEntity e) {
        return new Societe(
                e.getId(),
                e.getUser().getId(),
                e.getSocieteCode(),
                e.getTypeSociete(),
                e.getRaisonSociale(),
                e.getIdentifiantUnique(),
                e.getAdresse(),
                e.getVille(),
                e.getEmailSociete(),
                e.getTelephoneSociete(),
                e.getRneDocumentPath(),
                e.getPatenteDocumentPath(),
                e.getCinGerantDocumentPath(),
                e.getManagerId(),
                e.isActive(),
                e.getCreatedAt()
        );
    }

}
