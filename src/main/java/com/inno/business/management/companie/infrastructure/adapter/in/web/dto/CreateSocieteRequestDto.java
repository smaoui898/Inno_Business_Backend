package com.inno.business.management.companie.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Données pour créer une société")
public record CreateSocieteRequestDto(
        @Schema(example = "BUSINESS") String typeSociete,
        @Schema(example = "Inno Corp SARL") String raisonSociale,
        String identifiantUnique,
        String adresse,
        String ville,
        String emailSociete,
        String telephoneSociete
) {}