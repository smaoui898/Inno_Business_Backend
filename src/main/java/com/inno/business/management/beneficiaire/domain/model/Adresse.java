package com.inno.business.management.beneficiaire.domain.model;

import java.util.UUID;

public class Adresse {

    private final UUID id;
    private final String rue;
    private final String ville;
    private final AdresseType type;
    private final boolean isDefault;

    public Adresse(UUID id, String rue, String ville, AdresseType type, boolean isDefault) {
        this.id = id;
        this.rue = rue;
        this.ville = ville;
        this.type = type != null ? type : AdresseType.HOME;
        this.isDefault = isDefault;
    }

    public UUID getId() {
        return id;
    }

    public String getRue() {
        return rue;
    }

    public String getVille() {
        return ville;
    }

    public AdresseType getType() {
        return type;
    }

    public boolean isDefault() {
        return isDefault;
    }
}
