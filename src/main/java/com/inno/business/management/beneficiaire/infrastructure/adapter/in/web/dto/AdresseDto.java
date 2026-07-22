package com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto;

import java.util.UUID;

public record AdresseDto(
        UUID id,
        String rue,
        String ville,
        String type, // HOME, WORK, OTHER
        boolean isDefault
        ) {

}
