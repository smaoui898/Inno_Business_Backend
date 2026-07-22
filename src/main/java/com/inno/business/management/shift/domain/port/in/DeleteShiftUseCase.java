package com.inno.business.management.shift.domain.port.in;

import java.util.UUID;

public interface DeleteShiftUseCase {
    // Supprime le shift ET désaffecte automatiquement tous les bénéficiaires
    //lorsque on utilise Shift execute(...) on tombe dans l'exception ShiftNotFoundException
    // de plus dans l'API REST la suppression donne la réponse 204 No content(vide) dans idéalement c'est un retour void
    void execute(String ownerEmail, UUID shiftId);
}
