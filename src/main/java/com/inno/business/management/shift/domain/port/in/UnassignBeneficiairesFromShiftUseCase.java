package com.inno.business.management.shift.domain.port.in;

import java.util.UUID;

public interface UnassignBeneficiairesFromShiftUseCase {
    record UnassignedResult(long nombreDesaffectes){
    }
    // Désaffecte tous les bénéficiaires SANS supprimer le shift
    UnassignedResult execute(String ownerEmail, UUID shiftId);
}
