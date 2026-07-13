package com.inno.business.management.companie.domain.exception;

public class SocieteNotFoundException extends RuntimeException {
    public SocieteNotFoundException(String id) {
        super("Societe avec l'ID " + id + " non trouvé!");
    }

}
