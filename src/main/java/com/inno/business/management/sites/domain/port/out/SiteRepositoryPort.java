package com.inno.business.management.sites.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.inno.business.management.sites.domain.model.Site;
//developper dans adapter infrastructure
public interface SiteRepositoryPort {
    //ici on met les operation sur sites
    List<Site> findAllBySocieteId(UUID societeId);
    // find by id
    Optional<Site> findById(UUID id);
    // save site
    Site save(Site site);
    // delete site
    void delete(UUID id);
    // exists by id and societe id
    boolean existsByIdAndSocieteId(UUID id, UUID societeId);
    // count beneficiaires by site id
    long countBeneficiairesBySiteId(UUID siteId);
}