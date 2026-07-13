// infrastructure/societe/web/dto/UpdateSocieteRequestDto.java
package com.inno.business.management.companie.infrastructure.adapter.in.web.dto;

public record UpdateSocieteRequestDto(
        String raisonSociale,
        String identifiantUnique,
        String adresse,
        String ville,
        String emailSociete,
        String telephoneSociete
        ) {

}
