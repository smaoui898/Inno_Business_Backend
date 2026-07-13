package com.inno.business.management.companie.domain.ports.in;

import java.util.List;
import java.util.UUID;

public interface UpdateManagerUseCase {

     record UpdateManagerCommand(
        String     ownerEmail,
        UUID       managerId,
        String     prenom,    
        String     nom,
        String     telephone,
        List<UUID> societeIds // liste complète des sociétés à assigner
    ) {}

    record UpdateManagerResult(
        UUID       managerId,
        String     prenom,
        String     nom,
        String     email,
        String     telephone,
        List<UUID> assignedSocieteIds
    ) {}

    UpdateManagerResult execute(UpdateManagerCommand command);
}
