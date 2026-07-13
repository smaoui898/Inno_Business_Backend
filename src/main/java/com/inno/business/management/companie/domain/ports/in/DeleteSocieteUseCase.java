package com.inno.business.management.companie.domain.ports.in;

import java.util.UUID;

public interface DeleteSocieteUseCase {
    void execute(String ownerEmail, UUID societeId);
}
