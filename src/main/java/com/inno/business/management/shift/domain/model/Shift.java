package com.inno.business.management.shift.domain.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Shift {

    private final UUID id;
    private final UUID ownerId;
    private String nom;
    private List<ShiftHoraire> horaires;
    private final LocalDateTime createdAt;

    public Shift(UUID id, UUID ownerId, String nom,
            List<ShiftHoraire> horaires, LocalDateTime createdAt) {

        if (ownerId == null) {
            throw new IllegalArgumentException("ownerId obligatoire");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom du shift est obligatoire");
        }
        if (horaires == null || horaires.isEmpty()) {
            throw new IllegalArgumentException("Au moins un jour d'horaire est requis");
        }
        if (horaires.stream().noneMatch(ShiftHoraire::hasAtLeastOneTime)) {
            throw new IllegalArgumentException(
                    "Au moins un jour doit avoir une heure d'arrivée ou de retour");
        }

        this.id = id;
        this.ownerId = ownerId;
        this.nom = nom;
        this.horaires = horaires;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public String getNom() {
        return nom;
    }

    public List<ShiftHoraire> getHoraires() {
        return horaires;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
