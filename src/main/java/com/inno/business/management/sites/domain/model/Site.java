package com.inno.business.management.sites.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Site {

    private final UUID id;
    private final UUID societeId;     // FK vers Company
    private final String nom;
    private final String adresse;
    private final String ville;
    private final String telephone;
    private final String email;
    private UUID responsableId; // nullable, mutable
    private final LocalDateTime createdAt;

    public Site(UUID id, UUID societeId, String nom, String adresse,
            String ville, String telephone, String email,
            UUID responsableId, LocalDateTime createdAt) {
        if (societeId == null) {
            throw new IllegalArgumentException("societeId obligatoire");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du site est obligatoire");
        }
        if (adresse == null || adresse.isBlank()) {
            throw new IllegalArgumentException("L'adresse est obligatoire");
        }
        if (telephone == null || telephone.isBlank()) {
            throw new IllegalArgumentException("Le téléphone est obligatoire");
        }

        this.id = id;
        this.societeId = societeId;
        this.nom = nom;
        this.adresse = adresse;
        this.ville = ville;
        this.telephone = telephone;
        this.email = email;
        this.responsableId = responsableId;
        this.createdAt = createdAt;

    }

    public void assignResponsable(UUID responsableId) {
        this.responsableId = responsableId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSocieteId() {
        return societeId;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public UUID getResponsableId() {
        return responsableId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
