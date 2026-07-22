package com.inno.business.management.shift.application;

import java.util.List;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.shift.domain.exception.ShiftNotFoundException;
import com.inno.business.management.shift.domain.model.Shift;
import com.inno.business.management.shift.domain.model.ShiftHoraire;
import com.inno.business.management.shift.domain.port.in.UpdateShiftUseCase;
import com.inno.business.management.shift.domain.port.out.ShiftRepositoryPort;

public class UpdateShiftUseCaseImpl implements UpdateShiftUseCase {

    private final UserRepositoryPort userRepository;
    private final ShiftRepositoryPort shiftRepository;

    public UpdateShiftUseCaseImpl(UserRepositoryPort userRepository, ShiftRepositoryPort shiftRepository) {
        this.userRepository = userRepository;
        this.shiftRepository = shiftRepository;
    }

    @Override
    public Shift execute(UpdateShiftCommand cmd) {
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (!shiftRepository.existsByIdAndOwnerId(cmd.shiftId(), owner.getId())) {
            throw new UnauthorizedAccessException();
        }

        Shift existing = shiftRepository.findById(cmd.shiftId())
                .orElseThrow(() -> new ShiftNotFoundException(cmd.shiftId().toString()));

        // Horaires mis à jour ou gardés tels quels
        List<ShiftHoraire> newHoraires = cmd.horaires() != null
                ? cmd.horaires().stream()
                        .map(h -> new ShiftHoraire(
                        UUID.randomUUID(),
                        h.jour(),
                        h.arriveeEntreprise(),
                        h.retourDomicile()
                ))
                        .toList()
                : existing.getHoraires();

        Shift updated = new Shift(
                existing.getId(),
                existing.getOwnerId(),
                cmd.nom() != null && !cmd.nom().isBlank() ? cmd.nom() : existing.getNom(),
                newHoraires,
                existing.getCreatedAt()
        );

        return shiftRepository.save(updated);
    }

}
