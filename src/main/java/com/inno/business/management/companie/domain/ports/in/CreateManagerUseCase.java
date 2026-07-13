package com.inno.business.management.companie.domain.ports.in;

import java.util.List;
import java.util.UUID;

public interface CreateManagerUseCase {

    record CreateManagerCommand(
        String ownerEmail,
        String prenom,
        String nom,
        String telephone,
        String email,
        String password,
        List<UUID> societeIds  // societes a associer au manager
    ) {}

    record CreateManagerResult(
        UUID managerId,
        String email,
        String prenom,
        String nom
    ) {}

    CreateManagerResult execute(CreateManagerCommand command);
}
