package com.inno.business.management.sites.infrastructure.adapter.in.web;

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
import org.springframework.web.bind.annotation.RestController;

import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import com.inno.business.auth.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.inno.business.management.sites.domain.model.Site;
import com.inno.business.management.sites.domain.port.in.CreateSiteUseCase;
import com.inno.business.management.sites.domain.port.in.DeleteSiteUseCase;
import com.inno.business.management.sites.domain.port.in.GetSitesUseCase;
import com.inno.business.management.sites.domain.port.in.UpdateSiteUseCase;
import com.inno.business.management.sites.infrastructure.adapter.in.web.dto.CreateSiteRequestDto;
import com.inno.business.management.sites.infrastructure.adapter.in.web.dto.SiteResponseDto;
import com.inno.business.management.sites.infrastructure.adapter.in.web.dto.UpdateSiteRequestDto;
import com.inno.business.management.sites.infrastructure.adapter.out.persistence.SpringSiteRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Sites", description = "Gestion des sites par société")
@SecurityRequirement(name = "bearerAuth")
public class SiteController {

    private final GetSitesUseCase getSitesUseCase;
    private final CreateSiteUseCase createSiteUseCase;
    private final UpdateSiteUseCase updateSiteUseCase;
    private final DeleteSiteUseCase deleteSiteUseCase;
    private final SpringSiteRepository siteRepo;
    private final SpringUserRepository userRepo;

    public SiteController(GetSitesUseCase getSitesUseCase,
            CreateSiteUseCase createSiteUseCase,
            UpdateSiteUseCase updateSiteUseCase,
            DeleteSiteUseCase deleteSiteUseCase,
            SpringSiteRepository siteRepo,
            SpringUserRepository userRepo) {
        this.getSitesUseCase = getSitesUseCase;
        this.createSiteUseCase = createSiteUseCase;
        this.updateSiteUseCase = updateSiteUseCase;
        this.deleteSiteUseCase = deleteSiteUseCase;
        this.siteRepo = siteRepo;
        this.userRepo = userRepo;
    }

    //  GET sites d'une société 
    @GetMapping("/api/societes/{societeId}/sites")
    @Operation(summary = "Sites d'une société")
    public ResponseEntity<List<SiteResponseDto>> getBySociete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID societeId) {

        List<SiteResponseDto> result = getSitesUseCase
                .execute(user.getUsername(), societeId)
                .stream()
                .map(s -> toDto(s, siteRepo.countBeneficiairesBySiteId(s.getId())))
                .toList();

        return ResponseEntity.ok(result);
    }

    //  POST nouveau site 
    @PostMapping("/api/societes/{societeId}/sites")
    @Operation(summary = "Créer un site",
            description = "Le responsable est optionnel — si renseigné, crée un compte ROLE_RESPONSABLE_SITE")
            //fel parametre mtaa kol fonctuin lezem l user wel dto mtaa kol model bch ye5ou donnée
    public ResponseEntity<SiteResponseDto> create(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID societeId,
            @RequestBody CreateSiteRequestDto dto) {

        Site site = createSiteUseCase.execute(new CreateSiteUseCase.CreateSiteCommand(
                user.getUsername(), societeId,
                dto.nom(), dto.adresse(), dto.ville(), dto.telephone(), dto.email(),
                dto.responsablePrenom(), dto.responsableNom(),
                dto.responsableTelephone(), dto.responsableEmail(), dto.responsablePassword()
        ));

        return ResponseEntity.status(201).body(toDto(site, 0));
    }

    //  PUT modifier un site 
    @PutMapping("/api/sites/{id}")
    @Operation(summary = "Modifier un site")
    public ResponseEntity<SiteResponseDto> update(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id,
            @RequestBody UpdateSiteRequestDto dto) {

        Site site = updateSiteUseCase.execute(new UpdateSiteUseCase.UpdateSiteCommand(
                user.getUsername(), id,
                dto.nom(), dto.adresse(), dto.ville(), dto.telephone(), dto.email()
        ));

        long count = siteRepo.countBeneficiairesBySiteId(id);
        return ResponseEntity.ok(toDto(site, count));
    }

    //  DELETE supprimer un site 
    @DeleteMapping("/api/sites/{id}")
    @Operation(summary = "Supprimer un site")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable UUID id) {

        deleteSiteUseCase.execute(user.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    //  Mapper 
    private SiteResponseDto toDto(Site s, long nombreBeneficiaires) {
        String respNom = null, respEmail = null, respTel = null;
        if (s.getResponsableId() != null) {
            UserJpaEntity resp = userRepo.findById(s.getResponsableId()).orElse(null);
            if (resp != null) {
                respNom = resp.getPrenom() + " " + resp.getNom();
                respEmail = resp.getEmail();
                respTel = resp.getTelephone();
            }
        }
        return new SiteResponseDto(
                s.getId(), s.getNom(), s.getAdresse(), s.getVille(),
                s.getTelephone(), s.getEmail(),
                s.getResponsableId(), respNom, respEmail, respTel,
                nombreBeneficiaires, s.getCreatedAt()
        );
    }

}
