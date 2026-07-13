package com.inno.business.management.beneficiaire.domain.port.in;

import java.util.List;
import java.util.UUID;

import com.inno.business.management.beneficiaire.domain.model.Adresse;
import com.inno.business.management.beneficiaire.domain.model.Beneficiaire;

public interface CreateBeneficiaireUseCase {
    //créer un nouveau bénéficiaire
    record CreateBeneficiaireCommand(
        String       ownerEmail,
        UUID         siteId,
        String       prenom,
        String       nom,
        String       email,
        String       telephone,
        String       cin,
        String       matricule,
        List<Adresse> adresses,
        String       typeCourse
    ) {}
    //execute la commande
    Beneficiaire execute(CreateBeneficiaireCommand command);
}
