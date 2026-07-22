package com.inno.business.management.shift.domain.model;

public enum JourSemaine {
    LUNDI, MARDI, MERCREDI, JEUDI, VENDREDI, SAMEDI, DIMANCHE;

    public static JourSemaine fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Jour invalide : null");
        }
        try {
            return JourSemaine.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Jour invalide : " + value);
        }
    }
}
