package com.inno.business.management.shift.application;

import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.shift.domain.exception.ShiftNotFoundException;
import com.inno.business.management.shift.domain.port.in.DeleteShiftUseCase;
import com.inno.business.management.shift.domain.port.out.ShiftRepositoryPort;

public class DeleteShiftUseCaseImpl implements DeleteShiftUseCase {

    private final UserRepositoryPort userRepository;
    private final ShiftRepositoryPort shiftRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;

    public DeleteShiftUseCaseImpl(UserRepositoryPort userRepository, ShiftRepositoryPort shiftRepository,
            BeneficiaireRepositoryPort beneficiaireRepository) {
        this.userRepository = userRepository;
        this.shiftRepository = shiftRepository;
        this.beneficiaireRepository = beneficiaireRepository;
    }
       @Override
    public void execute(String ownerEmail, UUID shiftId) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!shiftRepository.existsByIdAndOwnerId(shiftId, owner.getId()))
            throw new UnauthorizedAccessException();

        shiftRepository.findById(shiftId)
                .orElseThrow(() -> new ShiftNotFoundException(shiftId.toString()));

        // 1. Désaffecter tous les bénéficiaires de ce shift
        beneficiaireRepository.unassignAllFromShift(shiftId);

        // 2. Supprimer le shift (cascade supprime aussi les ShiftHoraires)
        shiftRepository.delete(shiftId);
    }
}
