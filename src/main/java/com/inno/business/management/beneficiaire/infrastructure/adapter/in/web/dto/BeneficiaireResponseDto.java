package com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record BeneficiaireResponseDto(
        UUID id,
        UUID siteId,
        String siteNom,
        String prenom,
        String nom,
        String email,
        String telephone,
        String cin,
        String matricule,
        List<AdresseDto> adresses,
        String typeCourse,
        String statut,
        LocalDateTime createdAt
        ) {

}
