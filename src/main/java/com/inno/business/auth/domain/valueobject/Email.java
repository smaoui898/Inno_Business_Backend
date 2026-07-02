package com.inno.business.auth.domain.valueobject;

public record Email(String value) {

    private static final String REGEX = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"; //expression régulière

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        if (!value.matches(REGEX)) {
            throw new IllegalArgumentException("Format d'email invalide : " + value);
        }
    }
}
