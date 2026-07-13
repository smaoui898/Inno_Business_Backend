package com.inno.business.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Company {

    private final UUID id;
    private final UUID userId;
    private final String societeCode;
    private final TypeSociete typeSociete;
    private final String nomGroupe;
    private final String raisonSociale;
    private final String identifiantUnique;
    private final String adresse;
    private final String ville;
    private final String emailSociete;
    private final String telephoneSociete;
    private final String rneDocumentPath;
    private final String patenteDocumentPath;
    private final String cinGerantDocumentPath;
    private final UUID managerId;
    private final boolean active;
    private final LocalDateTime createdAt;

    public Company(UUID id, UUID userId, TypeSociete typeSociete, String societeCode, String nomGroupe, String raisonSociale, String identifiantUnique, String adresse, String ville, String emailSociete, String telephoneSociete, String rneDocumentPath, String patenteDocumentPath, LocalDateTime createdAt, boolean active, java.lang.String cinGerantDocumentPath, java.util.UUID managerId) {

        if (userId == null) {
            throw new IllegalArgumentException("L'identifiant utilisateur est obligatoire");
        }
        if (typeSociete == null) {
            throw new IllegalArgumentException("Le type de société est obligatoire");
        }
        if (nomGroupe == null || nomGroupe.isBlank()) {
            throw new IllegalArgumentException("Le nom du groupe est obligatoire");
        }
        if (raisonSociale == null || raisonSociale.isBlank()) {
            throw new IllegalArgumentException("La raison sociale est obligatoire");
        }

        this.id = id;
        this.userId = userId;
        this.societeCode = societeCode;
        this.typeSociete = typeSociete;
        this.nomGroupe = nomGroupe;
        this.raisonSociale = raisonSociale;
        this.identifiantUnique = identifiantUnique;
        this.adresse = adresse;
        this.ville = ville;
        this.emailSociete = emailSociete;
        this.telephoneSociete = telephoneSociete;
        this.rneDocumentPath = rneDocumentPath;
        this.patenteDocumentPath = patenteDocumentPath;
        this.cinGerantDocumentPath = cinGerantDocumentPath;
        this.managerId = managerId;
        this.active = active;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getSocieteCode() {
        return societeCode;
    }

    public TypeSociete getTypeSociete() {
        return typeSociete;
    }

    public String getNomGroupe() {
        return nomGroupe;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public String getIdentifiantUnique() {
        return identifiantUnique;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getEmailSociete() {
        return emailSociete;
    }

    public String getTelephoneSociete() {
        return telephoneSociete;
    }

    public String getRneDocumentPath() {
        return rneDocumentPath;
    }

    public String getPatenteDocumentPath() {
        return patenteDocumentPath;
    }

    public String getCinGerantDocumentPath() {
        return cinGerantDocumentPath;
    }

    public UUID getManagerId() {
        return managerId;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
