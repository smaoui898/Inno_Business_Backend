package com.inno.business.management.sites.infrastructure.adapter.in.web.dto;
//reception les données de site créer
//ilammed 2 groupe (site + responsable) en un seul appel API

public record CreateSiteRequestDto(
        // Infos site
        String nom,
        String adresse,
        String ville,
        String telephone,
        String email,
        // Responsable (optionnel)
        String responsablePrenom,
        String responsableNom,
        String responsableTelephone,
        String responsableEmail,
        String responsablePassword
) {}
