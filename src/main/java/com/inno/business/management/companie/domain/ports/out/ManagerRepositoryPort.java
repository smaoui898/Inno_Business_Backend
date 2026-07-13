package com.inno.business.management.companie.domain.ports.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;
//pour developper dans adapter / persistance
public interface ManagerRepositoryPort {
    User              createManager(User manager);    // INSERT (sans ID)
    User              updateManager(User manager);    // UPDATE (avec ID)
    List<User>        findAllByCreatedBy(UUID createdByUserId);
    Optional<User>    findById(UUID id);
    boolean           existsByEmail(String email);
    void              deleteById(UUID id);
}
