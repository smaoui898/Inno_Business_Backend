package com.inno.business.management.companie.domain.ports.in;

import java.util.UUID;

import com.inno.business.management.companie.domain.model.Societe;

public interface UpdateSocieteUseCase {
    record UpdateSocieteCommand(
        String ownerEmail,
        UUID   societeId,
        String raisonSociale,
        String identifiantUnique,
        String adresse,
        String ville,
        String emailSociete,
        String telephoneSociete
    ) {}

    Societe execute(UpdateSocieteCommand command);
}
