package com.inno.business.management.shift.infrastructure.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Shift avec compteur de bénéficiaires")
public record ShiftResponseDto(
        UUID id,
        String nom,
        long nombreBeneficiaires,
        String createdAt, // format "dd/MM/yyyy"
        List<ShiftHoraireDto> horaires
        ) {

}
