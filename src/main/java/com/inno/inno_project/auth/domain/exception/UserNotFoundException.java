package com.inno.inno_project.auth.domain.exception;

public class UserNotFoundException extends RuntimeException {

    //ceci herite de RuntimeException qui herite de exception
    public UserNotFoundException(String email) {
        super("Utilisateur non trouvé : " + email);
    }

}
