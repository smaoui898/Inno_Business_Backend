package com.inno.business.auth.infrastructure.adapter.out.persistence;

import java.time.LocalDateTime;
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
import jakarta.persistence.OneToOne;
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
    // relation one to one  pour le user  (1user = 1 company)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserJpaEntity user;

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

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // ── Mapping Domain → JPA ──────────────────────────────────────────
    public static CompanyJpaEntity fromDomain(Company c, UserJpaEntity userEntity) {
        CompanyJpaEntity e = new CompanyJpaEntity();
        e.id                    = c.getId();
        e.user                  = userEntity;
        e.typeSociete           = c.getTypeSociete();
        e.nomGroupe             = c.getNomGroupe();
        e.raisonSociale         = c.getRaisonSociale();
        e.identifiantUnique     = c.getIdentifiantUnique();
        e.adresse               = c.getAdresse();
        e.ville                 = c.getVille();
        e.emailSociete          = c.getEmailSociete();
        e.telephoneSociete      = c.getTelephoneSociete();
        e.rneDocumentPath       = c.getRneDocumentPath();
        e.patenteDocumentPath   = c.getPatenteDocumentPath();
        e.cinGerantDocumentPath = c.getCinGerantDocumentPath();
        e.createdAt             = c.getCreatedAt();
        return e;
    }

    // ── Mapping JPA → Domain ──────────────────────────────────────────
    public Company toDomain() {
        return new Company(
                id,
                user.getId(),          // userId extrait de la relation one to one 
                typeSociete,
                nomGroupe, raisonSociale, identifiantUnique,
                adresse, ville, emailSociete, telephoneSociete,
                rneDocumentPath, patenteDocumentPath, cinGerantDocumentPath,
                createdAt
        );
    }
}