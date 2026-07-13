package com.inno.business.management.sites.domain.port.in;


import java.util.UUID;

import com.inno.business.management.sites.domain.model.Site;

public interface CreateSiteUseCase {

    record CreateSiteCommand(
        String ownerEmail,
        UUID   societeId,
        // Infos site
        String nom,
        String adresse,
        String ville,
        String telephone,
        String email,
        // Responsable (optionnel — tous peuvent être null)
        String responsablePrenom,
        String responsableNom,
        String responsableTelephone,
        String responsableEmail,
        String responsablePassword
    ) {}

    Site execute(CreateSiteCommand command);
}