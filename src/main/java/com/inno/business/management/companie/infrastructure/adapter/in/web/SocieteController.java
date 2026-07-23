package com.inno.business.management.companie.infrastructure.adapter.in.web;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.AssignManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.CreateSocieteUseCase;
import com.inno.business.management.companie.domain.ports.in.DeleteSocieteUseCase;
import com.inno.business.management.companie.domain.ports.in.GetSocietesUseCase;
import com.inno.business.management.companie.domain.ports.in.UpdateSocieteUseCase;
import com.inno.business.management.companie.infrastructure.adapter.in.web.dto.AssignManagerRequestDto;
import com.inno.business.management.companie.infrastructure.adapter.in.web.dto.SocieteResponseDto;
import com.inno.business.management.companie.infrastructure.adapter.in.web.dto.UpdateSocieteRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/societes")
@Tag(name = "Sociétés", description = "Gestion des sociétés du groupe")
@SecurityRequirement(name = "bearerAuth")
public class SocieteController {

    private final GetSocietesUseCase    getSocietesUseCase;
    private final CreateSocieteUseCase  createSocieteUseCase;
    private final UpdateSocieteUseCase  updateSocieteUseCase;
    private final DeleteSocieteUseCase  deleteSocieteUseCase;
    private final AssignManagerUseCase  assignManagerUseCase;

    public SocieteController(GetSocietesUseCase getSocietesUseCase,
                             CreateSocieteUseCase createSocieteUseCase,
                             UpdateSocieteUseCase updateSocieteUseCase,
                             DeleteSocieteUseCase deleteSocieteUseCase,
                             AssignManagerUseCase assignManagerUseCase) {
        this.getSocietesUseCase   = getSocietesUseCase;
        this.createSocieteUseCase = createSocieteUseCase;
        this.updateSocieteUseCase = updateSocieteUseCase;
        this.deleteSocieteUseCase = deleteSocieteUseCase;
        this.assignManagerUseCase = assignManagerUseCase;
    }

    // GET toutes les sociétés du groupe 
    @GetMapping
    @Operation(summary = "Mes sociétés", description = "Retourne toutes les sociétés du groupe")
    public ResponseEntity<List<SocieteResponseDto>> getAll(
            @AuthenticationPrincipal UserDetails user) {

        List<SocieteResponseDto> result = getSocietesUseCase
                .execute(user.getUsername())
                .stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }

    //POST nouvelle société 
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Créer une société",
               description = "Ajoute une nouvelle société au groupe (docs requis)")
    public ResponseEntity<SocieteResponseDto> create(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam String typeSociete,
            @RequestParam String raisonSociale,
            @RequestParam(required = false) String identifiantUnique,
            @RequestParam(required = false) String adresse,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String emailSociete,
            @RequestParam(required = false) String telephoneSociete,
            @RequestParam MultipartFile rneDocument,
            @RequestParam MultipartFile patenteDocument,
            @RequestParam MultipartFile cinGerantDocument
    ) throws IOException {

        Societe societe = createSocieteUseCase.execute(
                new CreateSocieteUseCase.CreateSocieteCommand(
                        user.getUsername(),
                        typeSociete, raisonSociale, identifiantUnique,
                        adresse, ville, emailSociete, telephoneSociete,
                        rneDocument.getBytes(),      rneDocument.getOriginalFilename(),
                        patenteDocument.getBytes(),  patenteDocument.getOriginalFilename(),
                        cinGerantDocument.getBytes(),cinGerantDocument.getOriginalFilename()
                )
        );

        return ResponseEntity.status(201).body(toDto(societe));
    }

    // PUT modifier une société 
    @PutMapping("/{id}")
    @Operation(summary = "Modifier une société")
    public ResponseEntity<SocieteResponseDto> update(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody UpdateSocieteRequestDto dto) {

        Societe updated = updateSocieteUseCase.execute(
                new UpdateSocieteUseCase.UpdateSocieteCommand(
                        user.getUsername(), id,
                        dto.raisonSociale(), dto.identifiantUnique(),
                        dto.adresse(), dto.ville(),
                        dto.emailSociete(), dto.telephoneSociete()
                )
        );

        return ResponseEntity.ok(toDto(updated));
    }

    // DELETE supprimer une société 
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une société")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id) {

        deleteSocieteUseCase.execute(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    // PUT assigner/désassigner un manager 
    @PutMapping("/{id}/manager")
    @Operation(summary = "Assigner un manager",
               description = "Passer managerId à null pour désassigner")
    public ResponseEntity<Void> assignManager(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody AssignManagerRequestDto dto) {

        assignManagerUseCase.execute(
                new AssignManagerUseCase.AssignManagerCommand(
                        user.getUsername(), id, dto.managerId()
                )
        );

        return ResponseEntity.ok().build();
    }

    //Mapper 
    private SocieteResponseDto toDto(Societe s) {
        return new SocieteResponseDto(
                s.getId(), s.getSocieteCode(),
                s.getTypeSociete().name(),
                s.getRaisonSociale(), s.getIdentifiantUnique(),
                s.getAdresse(), s.getVille(),
                s.getEmailSociete(), s.getTelephoneSociete(),
                s.getManagerId(), s.isActive(), s.getCreatedAt()
        );
    }
}