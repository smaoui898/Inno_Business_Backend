package com.inno.business.auth.infrastructure.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.inno.business.auth.domain.exception.InvalidCredentialsException;
import com.inno.business.auth.domain.exception.PasswordMismatchException;
import com.inno.business.auth.domain.exception.UserAlreadyExistsException;
import com.inno.business.management.companie.domain.exception.SocieteNotFoundException;
import com.inno.business.management.companie.domain.exception.UnauthorizedAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(InvalidCredentialsException e) {
        return error(HttpStatus.UNAUTHORIZED, e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserExists(UserAlreadyExistsException e) {
        return error(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<Map<String, String>> handlePasswordMismatch(PasswordMismatchException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleValidation(IllegalArgumentException e) {
        return error(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException e) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    private ResponseEntity<Map<String, String>> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(Map.of("error", message));
    }


@ExceptionHandler(SocieteNotFoundException.class)
public ResponseEntity<Map<String, String>> handleSocieteNotFound(SocieteNotFoundException e) {
    return error(HttpStatus.NOT_FOUND, e.getMessage());
}

@ExceptionHandler(UnauthorizedAccessException.class)
public ResponseEntity<Map<String, String>> handleUnauthorized(UnauthorizedAccessException e) {
    return error(HttpStatus.FORBIDDEN, e.getMessage());
}
}