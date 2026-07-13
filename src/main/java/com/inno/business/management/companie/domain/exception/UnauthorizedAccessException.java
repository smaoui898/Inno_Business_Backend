package com.inno.business.management.companie.domain.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("Accès non autorisé");
    }

}
