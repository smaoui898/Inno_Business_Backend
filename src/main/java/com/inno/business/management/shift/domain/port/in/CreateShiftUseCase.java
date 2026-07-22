package com.inno.business.management.shift.domain.port.in;

import java.util.List;

import com.inno.business.management.shift.domain.model.JourSemaine;
import com.inno.business.management.shift.domain.model.Shift;

public interface CreateShiftUseCase {

    record HoraireCommand(JourSemaine jour, String arriveeEntreprise, String retourDomicile) {
    }

    record CreateShiftCommand(String ownerEmail, String nom, List<HoraireCommand> horaires) {
    }
    Shift execute(CreateShiftCommand command);
}
