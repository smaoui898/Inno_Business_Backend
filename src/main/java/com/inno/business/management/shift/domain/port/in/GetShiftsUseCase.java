package com.inno.business.management.shift.domain.port.in;

import java.util.List;

import com.inno.business.management.shift.domain.model.Shift;

public interface GetShiftsUseCase {
    // ShiftWithCount : structure pour retourner un Shift avec le nombre de bénéficiaires
    record ShiftWithCount(Shift shift, long nombreBeneficiaires) {
    }

    // Requête : GetShiftsRequest{ownerEmail='admin@test.com'}
    List<ShiftWithCount> execute(String ownerEmail);
}
