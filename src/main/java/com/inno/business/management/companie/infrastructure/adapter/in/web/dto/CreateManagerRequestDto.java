package com.inno.business.management.companie.infrastructure.adapter.in.web.dto;

import java.util.List;
import java.util.UUID;

public record CreateManagerRequestDto(
        String      prenom,
        String      nom,
        String      email,
        String      telephone,
        String      password,
        List<UUID>  societeIds
) {}