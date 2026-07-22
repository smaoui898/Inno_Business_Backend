package com.inno.business.management.sites.infrastructure.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.business.auth.infrastructure.adapter.out.persistence.CompanyJpaEntity;
import com.inno.business.management.sites.domain.model.Site;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "sites")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SiteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    // ManyToOne : Societe peut avoir plusieur site 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "societe_id", nullable = false)
    private CompanyJpaEntity societe;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    private String ville;

    @Column(nullable = false)
    private String telephone;

    @Column(name = "email_site")
    private String email;

    @Column(name = "responsable_id")
    private UUID responsableId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Mapping Domain → JPA
    public static SiteJpaEntity fromDomain(Site s, CompanyJpaEntity societeEntity) {
        SiteJpaEntity e = new SiteJpaEntity();
        e.id             = s.getId();
        e.societe        = societeEntity;
        e.nom            = s.getNom();
        e.adresse        = s.getAdresse();
        e.ville          = s.getVille();
        e.telephone      = s.getTelephone();
        e.email          = s.getEmail();
        e.responsableId  = s.getResponsableId();
        e.createdAt      = s.getCreatedAt();
        return e;
    }

    // Mapping JPA → Domain
    public Site toDomain() {
        return new Site(
                id, societe.getId(), nom, adresse, ville, telephone,
                email, responsableId, createdAt
        );
    }
}