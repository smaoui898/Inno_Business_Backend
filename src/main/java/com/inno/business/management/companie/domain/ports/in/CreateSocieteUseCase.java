package com.inno.business.management.companie.domain.ports.in;

import com.inno.business.management.companie.domain.model.Societe;

public interface CreateSocieteUseCase {
    record CreateSocieteCommand(
        String ownerEmail,
        String typeSociete,
        String raisonSociale,
        String identifiantUnique,
        String adresse,
        String ville,
        String emailSociete,
        String telephoneSociete,
        byte[] rneData,      String rneName,
        byte[] patenteData,  String patenteName,
        byte[] cinData,      String cinName
    ) {}

    
    Societe execute(CreateSocieteCommand command);
}
