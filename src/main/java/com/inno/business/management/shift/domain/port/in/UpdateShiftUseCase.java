package com.inno.business.management.shift.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.inno.business.management.shift.domain.model.JourSemaine;
import com.inno.business.management.shift.domain.model.Shift;

public interface UpdateShiftUseCase {

    record HoraireCommand(JourSemaine jour, String arriveeEntreprise, String retourDomicile) {
    }
    record UpdateShiftCommand(String ownerEmail, UUID shiftId, String nom, List<HoraireCommand> horaires) {
    }
    Shift execute(UpdateShiftCommand command);
}
