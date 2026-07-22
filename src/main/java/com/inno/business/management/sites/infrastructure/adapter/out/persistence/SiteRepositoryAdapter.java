package com.inno.business.management.sites.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

import com.inno.business.auth.infrastructure.adapter.out.persistence.CompanyJpaEntity;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.sites.domain.model.Site;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;

@Component
public class SiteRepositoryAdapter implements SiteRepositoryPort {

    private final SpringSiteRepository    siteRepo;
    private final SpringCompanyRepository companyRepo;

    public SiteRepositoryAdapter(SpringSiteRepository siteRepo,
                                 SpringCompanyRepository companyRepo) {
        this.siteRepo    = siteRepo;
        this.companyRepo = companyRepo;
    }

    @Override
    public List<Site> findAllBySocieteId(UUID societeId) {
        return siteRepo.findAllBySocieteId(societeId)
                .stream().map(SiteJpaEntity::toDomain).toList();
    }

    @Override
    public Optional<Site> findById(UUID id) {
        return siteRepo.findById(id).map(SiteJpaEntity::toDomain);
    }

    @Override
    public Site save(Site site) {
        CompanyJpaEntity company = companyRepo.findById(site.getSocieteId())
                .orElseThrow(() -> new RuntimeException("Société non trouvée"));

        SiteJpaEntity entity;

        if (site.getId() != null) {
            // Mise à jour : l'entité existe déjà en BDD → on la charge et on la modifie
            entity = siteRepo.findById(site.getId()).orElse(new SiteJpaEntity());
            entity.setId(site.getId());
        } else {
            // Création : pas d'ID → new entité vide, @GeneratedValue génère l'UUID
            entity = new SiteJpaEntity();
        }

        entity.setSociete(company);
        entity.setNom(site.getNom());
        entity.setAdresse(site.getAdresse());
        entity.setVille(site.getVille());
        entity.setTelephone(site.getTelephone());
        entity.setEmail(site.getEmail());
        entity.setResponsableId(site.getResponsableId());
        if (entity.getCreatedAt() == null) entity.setCreatedAt(site.getCreatedAt());

        return siteRepo.save(entity).toDomain();
    }

    @Override
    public void delete(UUID id) { siteRepo.deleteById(id); }

    @Override
    public boolean existsByIdAndSocieteId(UUID id, UUID societeId) {
        return siteRepo.existsByIdAndSocieteId(id, societeId);
    }

    @Override
    public long countBeneficiairesBySiteId(UUID siteId) {
        return siteRepo.countBeneficiairesBySiteId(siteId);
    }
}