package com.inno.business.management.shift.infrastructure.adapter.in.web.dto;


import java.util.List;

public record UpdateShiftRequestDto(
        String nom,                     // null = garder existant
        List<ShiftHoraireDto> horaires  // null = garder existants
) {}
