package com.inno.business.management.sites.infrastructure.adapter.in.web.dto;

public record UpdateSiteRequestDto(
        String nom,
        String adresse,
        String ville,
        String telephone,
        String email
) {}