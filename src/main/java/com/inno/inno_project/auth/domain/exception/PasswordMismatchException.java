package com.inno.inno_project.auth.domain.exception;

public class PasswordMismatchException extends RuntimeException {

    public PasswordMismatchException() {
        super("Les mots de passe ne correspondent pas");
    }
}
