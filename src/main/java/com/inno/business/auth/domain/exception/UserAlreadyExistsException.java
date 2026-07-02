
package com.inno.business.auth.domain.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String email) {
        super("Un compte existe déjà avec cet email : " + email);
    }
}