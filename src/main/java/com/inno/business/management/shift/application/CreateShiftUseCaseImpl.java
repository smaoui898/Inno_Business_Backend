package com.inno.business.management.shift.application;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.shift.domain.model.Shift;
import com.inno.business.management.shift.domain.model.ShiftHoraire;
import com.inno.business.management.shift.domain.port.in.CreateShiftUseCase;
import com.inno.business.management.shift.domain.port.out.ShiftRepositoryPort;

public class CreateShiftUseCaseImpl implements CreateShiftUseCase {
    private final UserRepositoryPort  userRepository;
    private final ShiftRepositoryPort shiftRepository;

    public CreateShiftUseCaseImpl(UserRepositoryPort userRepository,ShiftRepositoryPort shiftRepository) {
        this.userRepository  = userRepository;
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Shift execute(CreateShiftCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Shift shift = new Shift(
                UUID.randomUUID(),
                owner.getId(),
                cmd.nom(),
                cmd.horaires().stream()
                        .map(h -> new ShiftHoraire(
                                UUID.randomUUID(),
                                h.jour(),
                                h.arriveeEntreprise(),
                                h.retourDomicile()
                        ))
                        .toList(),
                LocalDateTime.now()
        );
        // save sont implementation dans infrastructure (adapter)
        return shiftRepository.save(shift);
    }

}
