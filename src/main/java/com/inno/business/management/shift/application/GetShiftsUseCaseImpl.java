package com.inno.business.management.shift.application;

import java.util.List;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.shift.domain.port.in.GetShiftsUseCase;
import com.inno.business.management.shift.domain.port.out.ShiftRepositoryPort;

public class GetShiftsUseCaseImpl implements GetShiftsUseCase {
    private final UserRepositoryPort         userRepository;
    private final ShiftRepositoryPort        shiftRepository;
    private final BeneficiaireRepositoryPort beneficiaireRepository;

    public GetShiftsUseCaseImpl(UserRepositoryPort userRepository,ShiftRepositoryPort shiftRepository,BeneficiaireRepositoryPort beneficiaireRepository) {
        this.userRepository         = userRepository;
        this.shiftRepository        = shiftRepository;
        this.beneficiaireRepository = beneficiaireRepository;
    }
     @Override
    public List<ShiftWithCount> execute(String ownerEmail) {
        User owner;
        owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return shiftRepository.findAllByOwnerId(owner.getId())
                .stream()
                .map(shift -> new ShiftWithCount(
                        shift,
                        beneficiaireRepository.countByShiftId(shift.getId())
                ))
                .toList();
    }
}
