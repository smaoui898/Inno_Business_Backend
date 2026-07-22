package com.inno.business.management.companie.infrastructure.adapter.out.persistance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;

@Component
public class ManagerRepositoryAdapter implements ManagerRepositoryPort {

    private final SpringUserRepository userRepo;

    public ManagerRepositoryAdapter(SpringUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // ── Créer ou mettre à jour un manager ────────────────────────────
    // Spring Data JPA fait INSERT si l'id n'existe pas, UPDATE sinon
    @Override
    public User createManager(User manager) {
        UserJpaEntity entity = UserJpaEntity.fromDomain(manager);
        return userRepo.save(entity).toDomain();
    }

    // ── Tous les managers créés par un owner 
    @Override
    public List<User> findAllByCreatedBy(UUID createdByUserId) {
        return userRepo.findAllByCreatedByUserId(createdByUserId)
                .stream()
                .map(UserJpaEntity::toDomain)
                .toList();
    }

    // ── Trouver un manager par son id 
    @Override
    public Optional<User> findById(UUID id) {
        return userRepo.findById(id)
                .map(UserJpaEntity::toDomain);
    }

    // ── Vérifier si un email est déjà utilisé 
    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }

    // ── Supprimer un manager 
    @Override
    public void deleteManager(UUID managerId) {
        userRepo.deleteById(managerId);
    }

    @Override
    public User updateManager(User manager) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteById(UUID id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}