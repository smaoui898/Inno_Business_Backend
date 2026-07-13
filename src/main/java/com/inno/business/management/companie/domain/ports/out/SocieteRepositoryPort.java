package com.inno.business.management.companie.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.inno.business.management.companie.domain.model.Societe;
//pour developper dans l'adapter aprés
public interface SocieteRepositoryPort {
    List<Societe>    findAllByOwnerId(UUID ownerId); // get
    List<Societe>    findAllByManagerId(UUID managerId);  
    Optional<Societe> findById(UUID id);
    Societe          save(Societe societe); // post / update
    void             delete(UUID id); // delete
    boolean          existsByIdAndOwnerId(UUID id, UUID ownerId); // exists
}
