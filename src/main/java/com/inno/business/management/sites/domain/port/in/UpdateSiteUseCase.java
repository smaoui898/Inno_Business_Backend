package com.inno.business.management.sites.domain.port.in;

import java.util.UUID;

import com.inno.business.management.sites.domain.model.Site;

public interface UpdateSiteUseCase {

    record UpdateSiteCommand(
            String ownerEmail,
            UUID siteId,
            String nom,
            String adresse,
            String ville,
            String telephone,
            String email
            ) {

    }

    Site execute(UpdateSiteCommand command);
}
