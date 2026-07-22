package com.inno.business.management.beneficiaire.infrastructure.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.model.StatutBeneficiaire;
import com.inno.business.management.beneficiaire.domain.model.TypeCourse;
import com.inno.business.management.sites.infrastructure.adapter.out.persistence.SiteJpaEntity;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "beneficiaires")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BeneficiaireJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    //ManyToOne pour plusieur beneficiaire appartient a un seul et unique site 
    //sur la relation site dans l'entité BeneficiaireJpaEntity
    @ManyToOne(fetch = FetchType.LAZY)
    //cle étrangère nommée site_id dans la table béneficiaires a ete ajouter de la table site
    @JoinColumn(name = "site_id", nullable = false)
    private SiteJpaEntity site;

    @Column(name = "societe_id", nullable = false)
    private UUID societeId;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false)
    private String nom;

    private String email;

    @Column(nullable = false)
    private String telephone;

    private String cin;
    private String matricule;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_course", nullable = false)
    private TypeCourse typeCourse = TypeCourse.STANDARD;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutBeneficiaire statut = StatutBeneficiaire.ACTIF;

    @Column(name = "activite_id")
    private UUID activiteId;

    @Column(name = "shift_id")
    private UUID shiftId;

    @OneToMany(mappedBy = "beneficiaire", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdresseJpaEntity> adresses = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Mapping JPA → Domain 
    public Beneficiaire toDomain() {
        return new Beneficiaire(
                id, site.getId(), societeId,
                prenom, nom, email, telephone, cin, matricule,
                adresses.stream().map(AdresseJpaEntity::toDomain).toList(),
                typeCourse, statut,
                activiteId, shiftId, createdAt
        );
    }
}