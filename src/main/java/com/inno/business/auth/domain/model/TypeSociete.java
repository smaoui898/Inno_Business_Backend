package com.inno.business.auth.domain.model;

public enum TypeSociete {
    BUSINESS, INSTANT;

    public static TypeSociete fromString(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("Le type de société est obligatoire");
        try {
            return TypeSociete.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                "Type de société invalide : '" + value + "'. Valeurs acceptées : BUSINESS, INSTANT");
        }
    }
}