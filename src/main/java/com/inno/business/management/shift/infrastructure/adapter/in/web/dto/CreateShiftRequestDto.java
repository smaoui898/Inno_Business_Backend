package com.inno.business.management.shift.infrastructure.adapter.in.web.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Données pour créer un shift")
public record CreateShiftRequestDto(
        @Schema(example = "Shift Matin") String nom,
        @Schema(description = "Jours actifs uniquement (au moins 1 requis)")
        List<ShiftHoraireDto> horaires
) {}