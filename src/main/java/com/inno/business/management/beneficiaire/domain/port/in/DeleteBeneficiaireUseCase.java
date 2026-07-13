package com.inno.business.management.beneficiaire.domain.port.in;

import java.util.UUID;

public interface DeleteBeneficiaireUseCase {
    void execute(String ownerEmail, UUID beneficiaireId);

}
