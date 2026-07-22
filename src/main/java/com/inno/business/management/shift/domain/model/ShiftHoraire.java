package com.inno.business.management.shift.domain.model;

import java.util.UUID;

public class ShiftHoraire {

    private final UUID id;
    private final JourSemaine jour;
    private final String arriveeEntreprise; // "HH:mm" ou null
    private final String retourDomicile;    // "HH:mm" ou null

    public ShiftHoraire(UUID id, JourSemaine jour,
            String arriveeEntreprise, String retourDomicile) {
        if (jour == null) {
            throw new IllegalArgumentException("Le jour est obligatoire");
        }
        this.id = id;
        this.jour = jour;
        this.arriveeEntreprise = arriveeEntreprise;
        this.retourDomicile = retourDomicile;
    }

    public boolean hasAtLeastOneTime() {
        return (arriveeEntreprise != null && !arriveeEntreprise.isBlank())
                || (retourDomicile != null && !retourDomicile.isBlank());
    }

    public UUID getId() {
        return id;
    }

    public JourSemaine getJour() {
        return jour;
    }

    public String getArriveeEntreprise() {
        return arriveeEntreprise;
    }

    public String getRetourDomicile() {
        return retourDomicile;
    }
}
