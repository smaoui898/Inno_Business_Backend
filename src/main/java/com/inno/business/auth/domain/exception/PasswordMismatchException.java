package com.inno.business.auth.domain.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Les mots de passe ne correspondent pas");
    }
}
