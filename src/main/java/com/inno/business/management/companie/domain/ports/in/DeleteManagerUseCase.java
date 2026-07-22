package com.inno.business.management.companie.domain.ports.in;

import java.util.UUID;

public interface DeleteManagerUseCase {

    // Supprime le manager ET désassigne toutes ses sociétés
    void execute(String ownerEmail, UUID managerId);
}
