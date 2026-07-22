package com.inno.business.management.shift.domain.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.inno.business.management.shift.domain.model.Shift;
//ces fonctions sont indispensables pour les UseCases  
// ces fonctions sont le contrat que le domaine impose a la base de données
public interface ShiftRepositoryPort {
    List<Shift>    findAllByOwnerId(UUID ownerId);
    Optional<Shift> findById(UUID id);
    Shift          save(Shift shift);
    void           delete(UUID id);
    boolean        existsByIdAndOwnerId(UUID id, UUID ownerId);

    
}
