package com.inno.business.auth.domain.exception;

public class InvalidCredentialsException extends RuntimeException {

    //ceci herite de RuntimeException qui herite de exception
    public InvalidCredentialsException() {
        super("Email ou mot de passe incorrect");
    }
}
