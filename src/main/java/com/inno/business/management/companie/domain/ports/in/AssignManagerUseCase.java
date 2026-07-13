package com.inno.business.management.companie.domain.ports.in;

import java.util.UUID;

public interface AssignManagerUseCase {
    record AssignManagerCommand(String ownerEmail, UUID societeId, UUID managerId) {}

    void execute(AssignManagerCommand command);
}
