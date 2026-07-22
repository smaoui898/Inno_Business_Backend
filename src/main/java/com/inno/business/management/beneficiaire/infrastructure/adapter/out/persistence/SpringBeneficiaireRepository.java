package com.inno.business.management.beneficiaire.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringBeneficiaireRepository extends JpaRepository<BeneficiaireJpaEntity, UUID> {
    //recupérer tout les beneficiaires d'un site spécifique
    List<BeneficiaireJpaEntity> findAllBySiteId(UUID siteId);
    //compter tout les beneficiaires d'un site
    @Query("SELECT COUNT(b) FROM BeneficiaireJpaEntity b WHERE b.site.id = :siteId")
    long countBySiteId(@Param("siteId") UUID siteId);
    // recherche dynamique de beneficiaires (pagination)
    //b.societeId   = :societeId : on ne récuprere que les beneficiaires de la société connecté (filtre obligatoire)
    //+filtre de recherche textuelle globale(optional)
    //+filtre matricule(optional)
    //+filtre type course(optional)
    //+filtre site(optional)
    @Query("""
        SELECT b FROM BeneficiaireJpaEntity b
        WHERE b.societeId = :societeId
          AND (:search IS NULL OR LOWER(b.prenom) LIKE %:search%
               OR LOWER(b.nom) LIKE %:search%
               OR LOWER(b.email) LIKE %:search%
               OR b.telephone LIKE %:search%)
          AND (:matricule IS NULL OR b.matricule LIKE %:matricule%)
          AND (:typeCourse IS NULL OR CAST(b.typeCourse AS string) = :typeCourse)
          AND (:siteId2 IS NULL OR b.site.id = :siteId2)
        """)
    Page<BeneficiaireJpaEntity> findWithFilters(
            @Param("societeId") UUID societeId,
            @Param("search")     String search,
            @Param("matricule")  String matricule,
            @Param("typeCourse") String typeCourse,
            @Param("siteId2")    UUID siteId,
            Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
           "FROM BeneficiaireJpaEntity b WHERE b.id = :id AND b.societeId = :societeId")
    boolean existsByIdAndSocieteId(@Param("id") UUID id, @Param("societeId") UUID societeId);
}