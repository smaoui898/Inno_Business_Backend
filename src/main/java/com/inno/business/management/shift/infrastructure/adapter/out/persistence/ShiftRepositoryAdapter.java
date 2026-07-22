package com.inno.business.management.shift.infrastructure.adapter.out.persistence;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.inno.business.management.shift.domain.model.Shift;
import com.inno.business.management.shift.domain.model.ShiftHoraire;
import com.inno.business.management.shift.domain.port.out.ShiftRepositoryPort;

@Component
public class ShiftRepositoryAdapter implements ShiftRepositoryPort {

    // Injection de dépendances
    private final SpringShiftRepository shiftRepo;
    private final SpringUserRepository userRepo;

    public ShiftRepositoryAdapter(SpringShiftRepository shiftRepo, SpringUserRepository userRepo) {
        this.shiftRepo = shiftRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<Shift> findAllByOwnerId(UUID ownerId) {
        return shiftRepo.findAllByOwnerId(ownerId)
                .stream().map(ShiftJpaEntity::toDomain).toList();
    }

    @Override
    public Optional<Shift> findById(UUID id) {
        return shiftRepo.findById(id).map(ShiftJpaEntity::toDomain);
    }

    @Override
    @org.springframework.transaction.annotation.Transactional
    public Shift save(Shift shift) {
        UserJpaEntity owner = userRepo.findById(shift.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner non trouvé"));

        // Cherche si le shift existe déjà en base
        boolean exists = shift.getId() != null && shiftRepo.existsById(shift.getId());

        ShiftJpaEntity entity;

        if (exists) {
            // ── MISE À JOUR : charge l'entité existante depuis la DB ──
            entity = shiftRepo.findById(shift.getId()).get();
            entity.setNom(shift.getNom());
            // Remplace les horaires
            entity.getHoraires().clear();
        } else {
            // ── CRÉATION : nouvelle entité sans ID (Hibernate génère l'UUID) ──
            entity = new ShiftJpaEntity();
            entity.setOwner(owner);
            entity.setNom(shift.getNom());
            entity.setCreatedAt(shift.getCreatedAt());
        }

        // Construit la liste des nouveaux horaires
        List<ShiftHoraireJpaEntity> newHoraires = new ArrayList<>();
        for (ShiftHoraire h : shift.getHoraires()) {
            ShiftHoraireJpaEntity horaireEntity = new ShiftHoraireJpaEntity();
            horaireEntity.setShift(entity);
            horaireEntity.setJour(h.getJour());
            horaireEntity.setArriveeEntreprise(h.getArriveeEntreprise());
            horaireEntity.setRetourDomicile(h.getRetourDomicile());
            newHoraires.add(horaireEntity);
        }
        entity.getHoraires().addAll(newHoraires);

        // Un seul save() — persist() pour création, merge() pour mise à jour
        return shiftRepo.save(entity).toDomain();
    }

    @Override
    public void delete(UUID id) {
        shiftRepo.deleteById(id);
    }

    @Override
    public boolean existsByIdAndOwnerId(UUID id, UUID ownerId) {
        return shiftRepo.existsByIdAndOwnerId(id, ownerId);
    }
}
