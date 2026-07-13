package com.inno.business.management.beneficiaire.domain.port.in;

import java.util.UUID;

public interface ExportBeneficiairesUseCase {
    byte[] execute(String ownerEmail, UUID siteId);
    byte[] generateTemplate();
}

