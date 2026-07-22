package com.inno.business.management.companie.application;

import java.util.List;
import java.util.UUID;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.auth.domain.valueobject.Email;
import com.inno.business.management.companie.domain.exception.SocieteNotFoundException;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.UpdateManagerUseCase;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

public class UpdateManagerUseCaseImpl implements UpdateManagerUseCase {
    private final UserRepositoryPort    userRepository;
    private final ManagerRepositoryPort managerRepository;
    private final SocieteRepositoryPort societeRepository;

    public UpdateManagerUseCaseImpl(UserRepositoryPort userRepository,
                                    ManagerRepositoryPort managerRepository,
                                    SocieteRepositoryPort societeRepository) {
        this.userRepository    = userRepository;
        this.managerRepository = managerRepository;
        this.societeRepository = societeRepository;
    }

    @Override
    public UpdateManagerResult execute(UpdateManagerCommand cmd) {

        // 1. Vérifications 
        User owner = userRepository.findByEmail(cmd.ownerEmail())
                .orElseThrow(() -> new RuntimeException("Owner non trouvé"));

        User manager = managerRepository.findById(cmd.managerId())
                .orElseThrow(() -> new RuntimeException("Manager non trouvé : " + cmd.managerId()));

        // Le manager doit avoir été créé par cet owner
        // On accepte aussi les managers dont createdByUserId est null (legacy / migration)
        if (manager.getCreatedByUserId() != null &&
            !owner.getId().equals(manager.getCreatedByUserId()))
            throw new UnauthorizedAccessException();

        // 2. Mise à jour des infos personnelles 
        User updatedManager = new User(
                manager.getId(),
                cmd.prenom()    != null ? cmd.prenom()    : manager.getPrenom(),
                cmd.nom()       != null ? cmd.nom()       : manager.getNom(),
                cmd.telephone() != null ? cmd.telephone() : manager.getTelephone(),
                new Email(manager.getEmailValue()),   // email non modifiable
                manager.getPassword(),
                manager.getRole(),
                manager.getCreatedAt(),
                manager.getCreatedByUserId()
        );
        managerRepository.updateManager(updatedManager);

        // 3. Réassignation des sociétés
        List<Societe> actuelles = societeRepository.findAllByManagerId(cmd.managerId());

        // Désassigner les sociétés qui ne sont plus dans la liste
        for (Societe s : actuelles) {
            if (!cmd.societeIds().contains(s.getId())) {
                societeRepository.save(buildWithManager(s, null));
            }
        }

        // Assigner les nouvelles sociétés
        List<UUID> assignedIds = cmd.societeIds().stream().filter(id -> {
            if (!societeRepository.existsByIdAndOwnerId(id, owner.getId()))
                throw new UnauthorizedAccessException();
            Societe s = societeRepository.findById(id)
                    .orElseThrow(() -> new SocieteNotFoundException(id.toString()));
            societeRepository.save(buildWithManager(s, cmd.managerId()));
            return true;
        }).toList();

        return new UpdateManagerResult(
                updatedManager.getId(),
                updatedManager.getPrenom(),
                updatedManager.getNom(),
                updatedManager.getEmailValue(),
                updatedManager.getTelephone(),
                assignedIds
        );
    }

    // Crée une nouvelle instance de Societe avec le managerId fourni
    private Societe buildWithManager(Societe s, UUID managerId) {
        return new Societe(
                s.getId(), s.getOwnerId(), s.getSocieteCode(),
                s.getTypeSociete(), s.getRaisonSociale(), s.getIdentifiantUnique(),
                s.getAdresse(), s.getVille(), s.getEmailSociete(), s.getTelephoneSociete(),
                s.getRneDocumentPath(), s.getPatenteDocumentPath(), s.getCinGerantDocumentPath(),
                managerId,       // ← seul champ qui change
                s.isActive(), s.getCreatedAt()
        );
    }

}
