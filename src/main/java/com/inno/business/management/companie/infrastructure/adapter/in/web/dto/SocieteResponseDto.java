package com.inno.business.management.companie.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Détails d'une société")
public record SocieteResponseDto(
        UUID          id,
        String        societeCode,
        String        typeSociete,
        String        raisonSociale,
        String        identifiantUnique,
        String        adresse,
        String        ville,
        String        emailSociete,
        String        telephoneSociete,
        UUID          managerId,
        boolean       active,
        LocalDateTime createdAt
) {}