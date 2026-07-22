package com.inno.business.management.shift.infrastructure.adapter.in.web;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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

import com.inno.business.management.shift.domain.model.JourSemaine;
import com.inno.business.management.shift.domain.model.Shift;
import com.inno.business.management.shift.domain.port.in.CreateShiftUseCase;
import com.inno.business.management.shift.domain.port.in.DeleteShiftUseCase;
import com.inno.business.management.shift.domain.port.in.GetShiftsUseCase;
import com.inno.business.management.shift.domain.port.in.UnassignBeneficiairesFromShiftUseCase;
import com.inno.business.management.shift.domain.port.in.UpdateShiftUseCase;
import com.inno.business.management.shift.infrastructure.adapter.in.web.dto.CreateShiftRequestDto;
import com.inno.business.management.shift.infrastructure.adapter.in.web.dto.ShiftHoraireDto;
import com.inno.business.management.shift.infrastructure.adapter.in.web.dto.ShiftResponseDto;
import com.inno.business.management.shift.infrastructure.adapter.in.web.dto.UpdateShiftRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
@RestController
@RequestMapping("/api/shifts")
@CrossOrigin(origins = "*")
@Tag(name = "Shifts", description = "Gestion des shifts et horaires")
//ma5dem chy ken bel bearer auth (token) ken bel header
@SecurityRequirement(name = "bearerAuth")
public class ShiftController {
    private final GetShiftsUseCase getShiftsUseCase;
    private final CreateShiftUseCase createShiftUseCase;
    private final UpdateShiftUseCase updateShiftUseCase;
    private final DeleteShiftUseCase deleteShiftUseCase;
    private final UnassignBeneficiairesFromShiftUseCase unassignUseCase;
    // convertir une date a un texte et un texte a une date
    //afficher un date selon un format (par example 22/07/2026 => 22 juillet 2026)
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    public ShiftController(
            GetShiftsUseCase getShiftsUseCase,
            CreateShiftUseCase createShiftUseCase,
            UpdateShiftUseCase updateShiftUseCase,
            DeleteShiftUseCase deleteShiftUseCase,
            UnassignBeneficiairesFromShiftUseCase unassignUseCase) {
        this.getShiftsUseCase   = getShiftsUseCase;
        this.createShiftUseCase = createShiftUseCase;
        this.updateShiftUseCase = updateShiftUseCase;
        this.deleteShiftUseCase = deleteShiftUseCase;
        this.unassignUseCase    = unassignUseCase;
    }

    //GET liste des shifts
    @GetMapping
    @Operation(summary = "Liste des shifts",
               description = "Retourne tous les shifts du groupe avec le nombre de bénéficiaires")
    public ResponseEntity<List<ShiftResponseDto>> getAll(
            @AuthenticationPrincipal UserDetails user) {

        List<ShiftResponseDto> result = getShiftsUseCase
                .execute(user.getUsername())
                .stream()
                .map(swc -> toDto(swc.shift(), swc.nombreBeneficiaires()))
                .toList();

        return ResponseEntity.ok(result);
    }

    // POST créer un shift
    @PostMapping
    @Operation(summary = "Créer un shift",
               description = "Au moins 1 jour avec une heure est requis")
    public ResponseEntity<ShiftResponseDto> create(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody CreateShiftRequestDto dto) {

        Shift shift = createShiftUseCase.execute(
                new CreateShiftUseCase.CreateShiftCommand(
                        user.getUsername(),
                        dto.nom(),
                        dto.horaires().stream()
                                .map(h -> new CreateShiftUseCase.HoraireCommand(
                                        JourSemaine.fromString(h.jour()),
                                        h.arriveeEntreprise(),
                                        h.retourDomicile()
                                ))
                                .toList()
                )
        );

        return ResponseEntity.status(201).body(toDto(shift, 0L));
    }

    //  PUT modifier un shift 
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un shift",
               description = "nom=null → garde existant. horaires=null → garde existants")
    public ResponseEntity<ShiftResponseDto> update(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody UpdateShiftRequestDto dto) {

        Shift shift = updateShiftUseCase.execute(
                new UpdateShiftUseCase.UpdateShiftCommand(
                        user.getUsername(),
                        id,
                        dto.nom(),
                        dto.horaires() == null ? null :
                                dto.horaires().stream()
                                        .map(h -> new UpdateShiftUseCase.HoraireCommand(
                                                JourSemaine.fromString(h.jour()),
                                                h.arriveeEntreprise(),
                                                h.retourDomicile()
                                        ))
                                        .toList()
                )
        );

        long count = getShiftsUseCase.execute(user.getUsername())
                .stream()
                .filter(swc -> swc.shift().getId().equals(id))
                .mapToLong(GetShiftsUseCase.ShiftWithCount::nombreBeneficiaires)
                .findFirst()
                .orElse(0L);

        return ResponseEntity.ok(toDto(shift, count));
    }

    //  DELETE supprimer un shift (+ désaffecte auto) 
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un shift",
               description = "Désaffecte automatiquement tous les bénéficiaires avant suppression")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id) {

        deleteShiftUseCase.execute(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    //  DELETE désaffecter tous les bénéficiaires (sans supprimer)
    @DeleteMapping("/{id}/beneficiaires")
    @Operation(summary = "Désaffecter tous les bénéficiaires",
               description = "Retire le shift de tous les bénéficiaires SANS supprimer le shift")
    public ResponseEntity<Map<String, Long>> unassignAll(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id) {

        UnassignBeneficiairesFromShiftUseCase.UnassignResult result =
                unassignUseCase.execute(user.getUsername(), id);

        return ResponseEntity.ok(
                Map.of("nombreDesaffectes", result.nombreDesaffectes())
        );
    }

    //Mapper 
    private ShiftResponseDto toDto(Shift shift, long count) {
        return new ShiftResponseDto(
                shift.getId(),
                shift.getNom(),
                count,
                shift.getCreatedAt() != null
                        ? shift.getCreatedAt().format(DATE_FMT) : null,
                shift.getHoraires().stream()
                        .map(h -> new ShiftHoraireDto(
                                h.getJour().name(),
                                h.getArriveeEntreprise(),
                                h.getRetourDomicile()
                        ))
                        .toList()
        );
    }
}
