package com.inno.business.management.beneficiaire.infrastructure.adapter.out.persistence;


import java.util.UUID;

import com.inno.business.management.beneficiaire.domain.model.Adresse;
import com.inno.business.management.beneficiaire.domain.model.AdresseType;

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
@Table(name = "adresses")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class AdresseJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    //ManyToOne pour une adresse appartient a un seul beneficiaire 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beneficiaire_id", nullable = false)
    private BeneficiaireJpaEntity beneficiaire;

    private String rue;
    private String ville;
    //enum pour le type d'adresse 
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdresseType type = AdresseType.HOME;
    //default adresse pour un beneficiaire
    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

    public static AdresseJpaEntity fromDomain(Adresse a, BeneficiaireJpaEntity beneficiaireEntity) {
        AdresseJpaEntity e = new AdresseJpaEntity();
        // ID non défini → @GeneratedValue génère l'UUID via persist()
        // Les adresses sont toujours recréées (liste clear() puis rebuild)
        e.beneficiaire = beneficiaireEntity;
        e.rue          = a.getRue();
        e.ville        = a.getVille();
        e.type         = a.getType();
        e.isDefault    = a.isDefault();
        return e;
    }

    public Adresse toDomain() {
        return new Adresse(id, rue, ville, type, isDefault);
    }
}