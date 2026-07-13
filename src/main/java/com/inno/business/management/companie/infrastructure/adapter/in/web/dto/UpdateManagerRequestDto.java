package com.inno.business.management.companie.infrastructure.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

public record UpdateManagerRequestDto(
        String prenom,
        String nom,
        String telephone,
        List<UUID> societeIds
        ) {
    // pas besoin de getters/setters/constructeurs ici, c’est un record

}
