package com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

public record CreateBeneficiaireRequestDto(
        UUID siteId,
        String prenom,
        String nom,
        String email,
        String telephone,
        String cin,
        String matricule,
        List<AdresseDto> adresses,
        String typeCourse
        ) {

}
