package com.inno.business.management.companie.infrastructure.adapter.out.persistance;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.inno.business.auth.domain.model.User;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;

@Component
public class ManagerRepositoryAdapter implements ManagerRepositoryPort {

    private final SpringUserRepository userRepo;

    public ManagerRepositoryAdapter(SpringUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User createManager(User manager) {
        // On NE SET PAS l'ID pour que @GeneratedValue génère un nouvel UUID
        // Si on passe l'ID, Hibernate fait un UPDATE au lieu d'un INSERT
        UserJpaEntity entity = new UserJpaEntity();
        entity.setPrenom(manager.getPrenom());
        entity.setNom(manager.getNom());
        entity.setTelephone(manager.getTelephone());
        entity.setEmail(manager.getEmailValue());
        entity.setPassword(manager.getPassword());
        entity.setRole(manager.getRole());
        entity.setCreatedByUserId(manager.getCreatedByUserId());
        entity.setCreatedAt(manager.getCreatedAt());
        return userRepo.save(entity).toDomain();
    }

    @Override
    public User updateManager(User manager) {
        // On charge l'entité existante et on met à jour ses champs
        // Hibernate fait automatiquement un UPDATE (pas d'INSERT)
        UserJpaEntity entity = userRepo.findById(manager.getId())
                .orElseThrow(() -> new RuntimeException("Manager non trouvé : " + manager.getId()));
        entity.setPrenom(manager.getPrenom());
        entity.setNom(manager.getNom());
        entity.setTelephone(manager.getTelephone());
        // Email non modifiable — on ne touche pas à entity.email
        return userRepo.save(entity).toDomain();
    }

    @Override
    public void deleteById(UUID id) {
        userRepo.deleteById(id);
    }

    @Override
    // find all managers created by user with user id
    public List<User> findAllByCreatedBy(UUID createdByUserId) {
        return userRepo.findAllByCreatedByUserId(createdByUserId)
                .stream()
                .map(UserJpaEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userRepo.findById(id).map(UserJpaEntity::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepo.existsByEmail(email);
    }
}
