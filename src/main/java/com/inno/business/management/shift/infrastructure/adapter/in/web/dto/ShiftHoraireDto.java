package com.inno.business.management.shift.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
//taa3mir fel swagger
@Schema(description = "Horaire d'un jour de la semaine")
public record ShiftHoraireDto(
        @Schema(example = "LUNDI") String jour,
        @Schema(example = "09:00") String arriveeEntreprise,  // null = non configuré
        @Schema(example = "17:00") String retourDomicile       // null = non configuré
) {}
