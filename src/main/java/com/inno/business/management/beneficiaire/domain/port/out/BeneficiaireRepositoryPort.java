package com.inno.business.management.beneficiaire.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.port.in.GetBeneficiairesUseCase;

public interface BeneficiaireRepositoryPort {
    //trouver tout les beneficiaires
    GetBeneficiairesUseCase.PagedResult findAll(UUID societeId,
                                                GetBeneficiairesUseCase.BeneficiaireFilter filter);
    //trouver un beneficiaire par son id
    Optional<Beneficiaire> findById(UUID id);
    //sauvegarder un beneficiaire
    Beneficiaire           save(Beneficiaire beneficiaire);
    //supprimer un beneficiaire
    void                   delete(UUID id);
    //verifier si un beneficiaire existe
    boolean                existsByIdAndSocieteId(UUID id, UUID societeId);
    //trouver tout les beneficiaires d'un site
    List<Beneficiaire>     findAllBySiteId(UUID siteId);
    //compter tout les beneficiaires d'un site
    long                   countBySiteId(UUID siteId);
}
