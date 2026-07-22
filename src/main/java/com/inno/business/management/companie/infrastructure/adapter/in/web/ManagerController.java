package com.inno.business.management.companie.infrastructure.adapter.in.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.ports.in.AssignManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.CreateManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.DeleteManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.UpdateManagerUseCase;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;
import com.inno.business.management.companie.infrastructure.adapter.in.web.dto.CreateManagerRequestDto;
import com.inno.business.management.companie.infrastructure.adapter.in.web.dto.ManagerResponseDto;
import com.inno.business.management.companie.infrastructure.adapter.in.web.dto.UpdateManagerRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/managers")
@CrossOrigin(origins = "*")
@Tag(name = "Managers", description = "Création et gestion des managers")
@SecurityRequirement(name = "bearerAuth")
public class ManagerController {

    private final CreateManagerUseCase createManagerUseCase;
    private final ManagerRepositoryPort managerRepository;
    private final UserRepositoryPort userRepository;
    private final UpdateManagerUseCase updateManagerUseCase;
    private final AssignManagerUseCase assignManagerUseCase;
    private final DeleteManagerUseCase deleteManagerUseCase;


    public ManagerController(CreateManagerUseCase createManagerUseCase, 
                             ManagerRepositoryPort managerRepository, 
                             UserRepositoryPort userRepository, 
                             UpdateManagerUseCase updateManagerUseCase,
                             AssignManagerUseCase assignManagerUseCase,
                             DeleteManagerUseCase deleteManagerUseCase) {
        this.createManagerUseCase = createManagerUseCase;
        this.managerRepository = managerRepository;
        this.userRepository = userRepository;
        this.updateManagerUseCase = updateManagerUseCase;
        this.assignManagerUseCase = assignManagerUseCase;
        this.deleteManagerUseCase = deleteManagerUseCase;
    }

    // POST créer un manager 
    @PostMapping
    @Operation(summary = "Créer un manager",
            description = "Crée un manager et l'assigne aux sociétés sélectionnées")
    public ResponseEntity<ManagerResponseDto> create(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody CreateManagerRequestDto dto) {

        CreateManagerUseCase.CreateManagerResult result = createManagerUseCase.execute(
                new CreateManagerUseCase.CreateManagerCommand(
                        user.getUsername(),
                        dto.prenom(), dto.nom(), dto.telephone(), 
                        dto.email(), dto.password(),
                        dto.societeIds()
                )
        );

        return ResponseEntity.status(201).body(
                new ManagerResponseDto(
                        result.managerId(), result.prenom(),
                        result.nom(), result.email(), null
                )
        );
    }

    // GET liste des managers du groupe 
    @GetMapping
    @Operation(summary = "Mes managers")
    public ResponseEntity<List<ManagerResponseDto>> getAll(
            @AuthenticationPrincipal UserDetails user) {

        User owner = userRepository.findByEmail(user.getUsername())
                .orElseThrow(() -> new RuntimeException("Non trouvé"));

        List<ManagerResponseDto> managers = managerRepository
                .findAllByCreatedBy(owner.getId())
                .stream()
                .map(m -> new ManagerResponseDto(
                m.getId(), m.getPrenom(), m.getNom(),
                m.getEmailValue(), m.getTelephone()))
                .toList();

        return ResponseEntity.ok(managers);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un manager",
            description = "Met à jour les infos + réassigne les sociétés (liste complète)")
    public ResponseEntity<ManagerResponseDto> update(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody UpdateManagerRequestDto dto) {

        UpdateManagerUseCase.UpdateManagerResult result = updateManagerUseCase.execute(
                new UpdateManagerUseCase.UpdateManagerCommand(
                        user.getUsername(),
                        id,
                        dto.prenom(),
                        dto.nom(),
                        dto.telephone(),
                        dto.societeIds() != null ? dto.societeIds() : List.<UUID>of()
                )
        );

        return ResponseEntity.ok(new ManagerResponseDto(
                result.managerId(), result.prenom(),
                result.nom(), result.email(), result.telephone()
        ));
    }

    // DELETE désassigner le manager d'une société (sans supprimer le manager)
    @DeleteMapping("/{societeId}/assign")
    @Operation(summary = "Retirer le manager d'une société",
            description = "Désassigne le manager de la société sans le supprimer")
    public ResponseEntity<Void> removeManagerFromSociete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID societeId) {

        assignManagerUseCase.execute(
                new AssignManagerUseCase.AssignManagerCommand(
                        user.getUsername(),
                        societeId,
                        null // managerId = null → désassigner
                )
        );
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
@Operation(summary = "Supprimer un manager",
           description = "Désassigne toutes ses sociétés puis supprime le compte")
public ResponseEntity<Void> delete(
        @AuthenticationPrincipal UserDetails user,
        @PathVariable UUID id) {

    deleteManagerUseCase.execute(user.getUsername(), id);
    return ResponseEntity.noContent().build();
}
}

