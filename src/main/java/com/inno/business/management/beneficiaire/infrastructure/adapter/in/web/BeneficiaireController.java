package com.inno.business.management.beneficiaire.infrastructure.adapter.in.web;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inno.business.management.beneficiaire.domain.model.Adresse;
import com.inno.business.management.beneficiaire.domain.model.AdresseType;
import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;
import com.inno.business.management.beneficiaire.domain.port.in.CreateBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.DeleteBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.ExportBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.GetBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.ImportBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.UpdateBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto.AdresseDto;
import com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto.BeneficiaireResponseDto;
import com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto.CreateBeneficiaireRequestDto;
import com.inno.business.management.beneficiaire.infrastructure.adapter.in.web.dto.PagedBeneficiairesDto;
import com.inno.business.management.sites.infrastructure.adapter.out.persistence.SpringSiteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/beneficiaires")
@CrossOrigin(origins = "*")
@Tag(name = "Bénéficiaires", description = "Gestion des bénéficiaires avec import/export Excel")
@SecurityRequirement(name = "bearerAuth")
public class BeneficiaireController {

    private final GetBeneficiairesUseCase getUseCase;
    private final CreateBeneficiaireUseCase createUseCase;
    private final UpdateBeneficiaireUseCase updateUseCase;
    private final DeleteBeneficiaireUseCase deleteUseCase;
    private final ImportBeneficiairesUseCase importUseCase;
    private final ExportBeneficiairesUseCase exportUseCase;
    private final SpringSiteRepository siteRepo;

    public BeneficiaireController(GetBeneficiairesUseCase getUseCase,
            CreateBeneficiaireUseCase createUseCase,
            UpdateBeneficiaireUseCase updateUseCase,
            DeleteBeneficiaireUseCase deleteUseCase,
            ImportBeneficiairesUseCase importUseCase,
            ExportBeneficiairesUseCase exportUseCase,
            SpringSiteRepository siteRepo) {
        this.getUseCase = getUseCase;
        this.createUseCase = createUseCase;
        this.updateUseCase = updateUseCase;
        this.deleteUseCase = deleteUseCase;
        this.importUseCase = importUseCase;
        this.exportUseCase = exportUseCase;
        this.siteRepo = siteRepo;
    }

    //  GET liste avec filtres et pagination 
    @GetMapping
    @Operation(summary = "Liste des bénéficiaires", description = "Filtres: search, matricule, typeCourse, siteId")
    public ResponseEntity<PagedBeneficiairesDto> getAll(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String matricule,
            @RequestParam(required = false) String typeCourse,
            @RequestParam(required = false) UUID siteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var filter = new GetBeneficiairesUseCase.BeneficiaireFilter(
                search, matricule, typeCourse, siteId, page, size);

        var result = getUseCase.execute(user.getUsername(), filter);

        List<BeneficiaireResponseDto> content = result.content()
                .stream().map(b -> toDto(b)).toList();

        return ResponseEntity.ok(new PagedBeneficiairesDto(
                content, result.totalElements(), result.totalPages(), result.currentPage()));
    }

    //  POST créer un bénéficiaire 
    @PostMapping
    @Operation(summary = "Ajouter un bénéficiaire")
    public ResponseEntity<BeneficiaireResponseDto> create(
            @AuthenticationPrincipal UserDetails user,
            @RequestBody CreateBeneficiaireRequestDto dto) {

        List<Adresse> adresses = dto.adresses() != null
                ? dto.adresses().stream().map(a -> new Adresse(
                a.id(), a.rue(), a.ville(),
                AdresseType.valueOf(a.type() != null ? a.type().toUpperCase() : "HOME"),
                a.isDefault())).toList()
                : List.of();

        Beneficiaire b = createUseCase.execute(
                new CreateBeneficiaireUseCase.CreateBeneficiaireCommand(
                        user.getUsername(), dto.siteId(),
                        dto.prenom(), dto.nom(), dto.email(), dto.telephone(),
                        dto.cin(), dto.matricule(), adresses, dto.typeCourse()
                ));

        return ResponseEntity.status(201).body(toDto(b));
    }

    //  PUT modifier 
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un bénéficiaire")
    public ResponseEntity<BeneficiaireResponseDto> update(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody CreateBeneficiaireRequestDto dto) {

        List<Adresse> adresses = dto.adresses() != null
                ? dto.adresses().stream().map(a -> new Adresse(
                a.id(), a.rue(), a.ville(),
                AdresseType.valueOf(a.type() != null ? a.type().toUpperCase() : "HOME"),
                a.isDefault())).toList()
                : null;

        Beneficiaire b = updateUseCase.execute(
                new UpdateBeneficiaireUseCase.UpdateBeneficiaireCommand(
                        user.getUsername(), id,
                        dto.prenom(), dto.nom(), dto.email(), dto.telephone(),
                        dto.cin(), dto.matricule(), adresses, dto.typeCourse(), null
                ));

        return ResponseEntity.ok(toDto(b));
    }

    //  DELETE 
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un bénéficiaire")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id) {

        deleteUseCase.execute(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    //  POST import Excel 
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Importer depuis Excel",
            description = "Fichier .xlsx avec les colonnes du modèle")
    public ResponseEntity<ImportBeneficiairesUseCase.ImportResult> importExcel(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam UUID siteId,
            @RequestParam MultipartFile file) throws IOException {

        var result = importUseCase.execute(
                new ImportBeneficiairesUseCase.ImportCommand(
                        user.getUsername(), siteId, file.getBytes()
                ));

        return ResponseEntity.ok(result);
    }

    //  GET export Excel  
    @GetMapping("/export")
    @Operation(summary = "Exporter en Excel")
    public ResponseEntity<byte[]> exportExcel(
            @AuthenticationPrincipal UserDetails user,
            @RequestParam(required = false) UUID siteId) {

        byte[] data = exportUseCase.execute(user.getUsername(), siteId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=beneficiaires.xlsx")
                .body(data);
    }

    //  GET modèle Excel (template vide) 
    @GetMapping("/modele")
    @Operation(summary = "Télécharger le modèle Excel")
    public ResponseEntity<byte[]> downloadTemplate() {

        byte[] data = exportUseCase.generateTemplate();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=modele_beneficiaires.xlsx")
                .body(data);
    }

    //  Mapper 
    private BeneficiaireResponseDto toDto(Beneficiaire b) {
        String siteNom = siteRepo.findById(b.getSiteId())
                .map(s -> s.getNom()).orElse(null);

        List<AdresseDto> adresseDtos = b.getAdresses().stream()
                .map(a -> new AdresseDto(a.getId(), a.getRue(), a.getVille(),
                a.getType().name(), a.isDefault()))
                .toList();

        return new BeneficiaireResponseDto(
                b.getId(), b.getSiteId(), siteNom,
                b.getPrenom(), b.getNom(), b.getEmail(), b.getTelephone(),
                b.getCin(), b.getMatricule(), adresseDtos,
                b.getTypeCourse().name(), b.getStatut().name(), b.getCreatedAt()
        );
    }
}
