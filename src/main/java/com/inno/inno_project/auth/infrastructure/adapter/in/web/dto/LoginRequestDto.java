package com.inno.inno_project.auth.infrastructure.adapter.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Donnees de connexion")
public record LoginRequestDto(
        @Schema(description = "Adresse email", example = "admin@inno.tn") //annotation de swagger pour la documentation
        String email,
        @Schema(description = "Mot de passe", example = "password123") //annotation de swagger pour la documentation
        String password
        ) {

}
