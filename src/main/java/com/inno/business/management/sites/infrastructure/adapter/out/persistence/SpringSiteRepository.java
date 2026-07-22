package com.inno.business.management.sites.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringSiteRepository extends JpaRepository<SiteJpaEntity, UUID> {

    List<SiteJpaEntity> findAllBySocieteId(UUID societeId);
    // pour garantir que hibernate exécute la condition exacte et correctement donc on utilise query jpql
    // ici on vérifie que le site posséde le societe id  et leur meme id
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
           "FROM SiteJpaEntity s WHERE s.id = :id AND s.societe.id = :societeId")
    boolean existsByIdAndSocieteId(@Param("id") UUID id, @Param("societeId") UUID societeId);
    
    //ici verifier que le site posséde des beneficiaires et on les compte seulement les béneficiaires previent de la table BeneficiaireJpaEntity
    @Query("SELECT COUNT(b) FROM BeneficiaireJpaEntity b WHERE b.site.id = :siteId")
    long countBeneficiairesBySiteId(@Param("siteId") UUID siteId);
}