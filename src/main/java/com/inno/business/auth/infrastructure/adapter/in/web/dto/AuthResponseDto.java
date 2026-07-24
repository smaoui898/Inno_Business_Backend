package com.inno.business.auth.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Reponse apres connexion reussie")
public record AuthResponseDto(
        String token,
        String refreshToken,
        String email,
        String role,
        String prenom,
        String nom
        ) {
}
