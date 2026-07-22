package com.inno.business.management.sites.infrastructure.adapter.in.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record SiteResponseDto(
        UUID          id,
        String        nom,
        String        adresse,
        String        ville,
        String        telephone,
        String        email,
        UUID          responsableId,
        String        responsableNom,
        String        responsableEmail,
        String        responsableTelephone,
        long          nombreBeneficiaires,
        LocalDateTime createdAt
) {}