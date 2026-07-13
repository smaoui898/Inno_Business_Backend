package com.inno.business.management.companie.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.business.auth.domain.model.TypeSociete;

public class Societe {

    private final UUID id;
    private final UUID ownerId;        // group owner (User avec ROLE_USER)
    private final String societeCode;    // code court #22871
    private final TypeSociete typeSociete;
    private final String raisonSociale;
    private final String identifiantUnique;
    private final String adresse;
    private final String ville;
    private final String emailSociete;
    private final String telephoneSociete;
    private final String rneDocumentPath;
    private final String patenteDocumentPath;
    private final String cinGerantDocumentPath;
    // nullable — manager assigné
    private final UUID managerId;
    private final boolean active;
    private final LocalDateTime createdAt;

    public Societe(UUID id, UUID ownerId, String societeCode,
            TypeSociete typeSociete, String raisonSociale,
            String identifiantUnique, String adresse, String ville,
            String emailSociete, String telephoneSociete,
            String rneDocumentPath, String patenteDocumentPath,
            String cinGerantDocumentPath, UUID managerId,
            boolean active, LocalDateTime createdAt) {

        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId obligatoire");
        }
        if (raisonSociale == null || raisonSociale.isBlank()) {
            throw new IllegalArgumentException("La raison sociale est obligatoire");
        }
        if (typeSociete == null) {
            throw new IllegalArgumentException("Le type est obligatoire");
        }

        this.id = id;
        this.ownerId = ownerId;
        this.societeCode = societeCode;
        this.typeSociete = typeSociete;
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

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getSocieteCode() {
        return societeCode;
    }

    public TypeSociete getTypeSociete() {
        return typeSociete;
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
