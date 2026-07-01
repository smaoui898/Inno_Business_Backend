package com.inno.inno_project.auth.domain.valueobject;

public record Email(String value) {

    private static final String REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!value.matches(REGEX)) {
            throw new IllegalArgumentException("Format d'email invalide : " + value);
        }
    }
}
