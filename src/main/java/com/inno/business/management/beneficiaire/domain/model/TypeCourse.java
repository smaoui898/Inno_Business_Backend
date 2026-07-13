package com.inno.business.management.beneficiaire.domain.model;

public enum TypeCourse {
    STANDARD,
    PREMIUM,
    PARTAGEE,
    EXPRESS;

    public static TypeCourse fromString(String value) {
        if (value == null || value.isBlank()) {
            return STANDARD;
        }
        try {
            return TypeCourse.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return STANDARD;
        }
    }
}
