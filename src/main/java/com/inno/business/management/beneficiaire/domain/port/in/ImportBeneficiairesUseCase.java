package com.inno.business.management.beneficiaire.domain.port.in;

import java.util.List;
import java.util.UUID;

public interface ImportBeneficiairesUseCase {
    record ImportCommand(String ownerEmail, UUID siteId, byte[] excelData) {}

    record ImportResult(int imported, int skipped, List<String> errors) {}

    ImportResult execute(ImportCommand command);
}
