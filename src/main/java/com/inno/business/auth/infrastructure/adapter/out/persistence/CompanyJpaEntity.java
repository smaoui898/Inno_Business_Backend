package com.inno.business.auth.infrastructure.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import com.inno.business.auth.domain.model.Company;
import com.inno.business.auth.domain.model.TypeSociete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // relation one to many  pour le user  (1user = plusieur company)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserJpaEntity user;

    // Code court auto-généré pour affichage (#22871)
    @Column(name = "societe_code", unique = true, nullable = false, columnDefinition = "varchar(255) default gen_random_uuid()::varchar")
    private String societeCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_societe", nullable = false)
    private TypeSociete typeSociete;

    @Column(name = "nom_groupe", nullable = false)
    private String nomGroupe;

    @Column(name = "raison_sociale", nullable = false)
    private String raisonSociale;

    @Column(name = "identifiant_unique")
    private String identifiantUnique;

    private String adresse;
    private String ville;

    @Column(name = "email_societe")
    private String emailSociete;

    @Column(name = "telephone_societe")
    private String telephoneSociete;

    @Column(name = "rne_document_path")
    private String rneDocumentPath;

    @Column(name = "patente_document_path")
    private String patenteDocumentPath;

    @Column(name = "cin_gerant_document_path")
    private String cinGerantDocumentPath;
    //pour le manager assigné (nullable)
    @Column(name = "manager_id")
    private UUID managerId;
    // pour le status de company (active ou inactive)
    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Mapping Domain → JPA ──────────────────────────────────────────
    public static CompanyJpaEntity fromDomain(Company c, UserJpaEntity userEntity) {
        CompanyJpaEntity e = new CompanyJpaEntity();
        e.id = c.getId();
        e.user = userEntity;
        e.societeCode = c.getSocieteCode() != null ? c.getSocieteCode() : String.valueOf(10000 + new Random().nextInt(90000));
        e.typeSociete = c.getTypeSociete();
        e.nomGroupe = c.getNomGroupe();
        e.raisonSociale = c.getRaisonSociale();
        e.identifiantUnique = c.getIdentifiantUnique();
        e.adresse = c.getAdresse();
        e.ville = c.getVille();
        e.emailSociete = c.getEmailSociete();
        e.telephoneSociete = c.getTelephoneSociete();
        e.rneDocumentPath = c.getRneDocumentPath();
        e.patenteDocumentPath = c.getPatenteDocumentPath();
        e.cinGerantDocumentPath = c.getCinGerantDocumentPath();
        e.managerId = c.getManagerId();
        e.active = c.isActive();
        e.createdAt = c.getCreatedAt();
        return e;
    }

    // ── Mapping JPA → Domain ──────────────────────────────────────────
    public Company toDomain() {
        return new Company(
                id,
                user.getId(),
                typeSociete, societeCode,
                nomGroupe, raisonSociale, identifiantUnique,
                adresse, ville, emailSociete, telephoneSociete,
                rneDocumentPath, patenteDocumentPath,
                createdAt, false, null, null
        );
    }
}
