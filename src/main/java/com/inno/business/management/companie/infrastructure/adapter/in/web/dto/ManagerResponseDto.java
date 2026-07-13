package com.inno.business.management.companie.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public record ManagerResponseDto(
        UUID   id,
        String prenom,
        String nom,
        String email,
        String telephone
) {}