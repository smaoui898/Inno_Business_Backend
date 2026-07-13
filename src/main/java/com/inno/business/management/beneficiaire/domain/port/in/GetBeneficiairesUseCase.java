package com.inno.business.management.beneficiaire.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;

public interface GetBeneficiairesUseCase {

    //filtre pour chercher les beneficiaires
    record BeneficiaireFilter(
        String searchTerm,
        String matricule,
        String typeCourse,
        UUID   siteId,
        int    page,
        int    size
    ) {}
    
    //execute le filtre de recherche
    record PagedResult(
        List<Beneficiaire> content,
        long totalElements,
        int  totalPages,
        int  currentPage
    ) {}
//execute le filtre de recherche
    PagedResult execute(String ownerEmail, BeneficiaireFilter filter);
}
