package com.inno.business.management.beneficiaire.domain.exception;

public class BeneficiaireNotFoundException extends RuntimeException {

    public BeneficiaireNotFoundException(String id) {
        super("Bénéficiaire introuvable : " + id);
    }
}
