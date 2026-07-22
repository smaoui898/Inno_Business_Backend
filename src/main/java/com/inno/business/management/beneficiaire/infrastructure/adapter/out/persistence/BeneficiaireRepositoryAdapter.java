package com.inno.business.management.beneficiaire.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.port.in.GetBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.sites.infrastructure.adapter.out.persistence.SiteJpaEntity;
import com.inno.business.management.sites.infrastructure.adapter.out.persistence.SpringSiteRepository;

@Component
public class BeneficiaireRepositoryAdapter implements BeneficiaireRepositoryPort {

    private final SpringBeneficiaireRepository beneficiaireRepo;
    private final SpringSiteRepository         siteRepo;

    public BeneficiaireRepositoryAdapter(SpringBeneficiaireRepository beneficiaireRepo,
                                         SpringSiteRepository siteRepo) {
        this.beneficiaireRepo = beneficiaireRepo;
        this.siteRepo         = siteRepo;
    }

    @Override
    public GetBeneficiairesUseCase.PagedResult findAll(
            UUID societeId, GetBeneficiairesUseCase.BeneficiaireFilter filter) {

        PageRequest pageable = PageRequest.of(
                Math.max(filter.page(), 0),
                filter.size() > 0 ? filter.size() : 10
        );

        String search = filter.searchTerm() != null && !filter.searchTerm().isBlank()
                ? filter.searchTerm().toLowerCase() : null;

        Page<BeneficiaireJpaEntity> page = beneficiaireRepo.findWithFilters(
                societeId,
                search,
                filter.matricule(),
                filter.typeCourse(),
                filter.siteId(),
                pageable
        );

        return new GetBeneficiairesUseCase.PagedResult(
                page.getContent().stream().map(BeneficiaireJpaEntity::toDomain).toList(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getNumber()
        );
    }

    @Override
    public Optional<Beneficiaire> findById(UUID id) {
        return beneficiaireRepo.findById(id).map(BeneficiaireJpaEntity::toDomain);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Beneficiaire save(Beneficiaire b) {
        SiteJpaEntity site = siteRepo.findById(b.getSiteId())
                .orElseThrow(() -> new RuntimeException("Site non trouvé"));

        BeneficiaireJpaEntity entity;

        if (b.getId() != null) {
            // Mise à jour : charge l'entité existante pour éviter merge() sur entité détachée
            entity = beneficiaireRepo.findById(b.getId()).orElse(new BeneficiaireJpaEntity());
            entity.setId(b.getId());
        } else {
            // Création : pas d'ID → @GeneratedValue génère l'UUID via persist()
            entity = new BeneficiaireJpaEntity();
        }

        entity.setSite(site);
        entity.setSocieteId(b.getSocieteId());
        entity.setPrenom(b.getPrenom());
        entity.setNom(b.getNom());
        entity.setEmail(b.getEmail());
        entity.setTelephone(b.getTelephone());
        entity.setCin(b.getCin());
        entity.setMatricule(b.getMatricule());
        entity.setTypeCourse(b.getTypeCourse());
        entity.setStatut(b.getStatut());
        entity.setActiviteId(b.getActiviteId());
        entity.setShiftId(b.getShiftId());
        if (entity.getCreatedAt() == null) entity.setCreatedAt(b.getCreatedAt());

        // 1. Sauvegarde le bénéficiaire pour obtenir son ID généré
        entity.getAdresses().clear();
        BeneficiaireJpaEntity savedEntity = beneficiaireRepo.save(entity);

        // 2. Ajoute les adresses avec référence vers l'entité persistée
        b.getAdresses().forEach(a -> savedEntity.getAdresses()
                .add(AdresseJpaEntity.fromDomain(a, savedEntity)));

        return beneficiaireRepo.save(savedEntity).toDomain();
    }

    @Override
    public void delete(UUID id) { beneficiaireRepo.deleteById(id); }

    @Override
    public boolean existsByIdAndSocieteId(UUID id, UUID societeId) {
        return beneficiaireRepo.existsByIdAndSocieteId(id, societeId);
    }

    @Override
    public List<Beneficiaire> findAllBySiteId(UUID siteId) {
        return beneficiaireRepo.findAllBySiteId(siteId)
                .stream().map(BeneficiaireJpaEntity::toDomain).toList();
    }

    @Override
    public long countBySiteId(UUID siteId) {
        return beneficiaireRepo.countBySiteId(siteId);
    }
}