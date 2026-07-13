package com.inno.business.management.beneficiaire.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.inno.business.management.beneficiaire.domain.model.Adresse;
import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;

public interface UpdateBeneficiaireUseCase {
    //update les informations d'un beneficiaire
    record UpdateBeneficiaireCommand(
        String       ownerEmail,
        UUID         beneficiaireId,
        String       prenom,
        String       nom,
        String       email,
        String       telephone,
        String       cin,
        String       matricule,
        List<Adresse> adresses,
        String       typeCourse,
        String       statut
    ) {}
    //execute la commande
    Beneficiaire execute(UpdateBeneficiaireCommand command);
}

