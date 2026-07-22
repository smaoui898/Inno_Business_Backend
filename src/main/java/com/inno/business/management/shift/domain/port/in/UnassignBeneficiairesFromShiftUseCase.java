package com.inno.business.management.shift.domain.port.in;

import java.util.UUID;

public interface UnassignBeneficiairesFromShiftUseCase {

    // record qui contient le nombre de bénéficiaires désaffectés
    record UnassignResult(long nombreDesaffectes) {}

    // Désaffecte tous les bénéficiaires SANS supprimer le shift
    UnassignResult execute(String ownerEmail, UUID shiftId);
}
