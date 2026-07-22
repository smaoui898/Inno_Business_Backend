package com.inno.business.management.shift.infrastructure.adapter.out.persistence;

import java.util.UUID;

import com.inno.business.management.shift.domain.model.JourSemaine;
import com.inno.business.management.shift.domain.model.ShiftHoraire;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shift_horaires")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftHoraireJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    //ManyToOne : le table shift_horaire aura une relation FK pointant vers la table  shift
    //Un shift peut avoir plusieurs horaires (main)

    @ManyToOne(fetch = FetchType.LAZY)
    //configurer la colonne de clé étrangère
    @JoinColumn(name = "shift_id", nullable = false)
    private ShiftJpaEntity shift;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private JourSemaine jour;

    @Column(name = "arrivee_entreprise")
    private String arriveeEntreprise; // "HH:mm" ou null

    @Column(name = "retour_domicile")
    private String retourDomicile;    // "HH:mm" ou null

    // Mapping JPA → Domain
    public ShiftHoraire toDomain() {
        return new ShiftHoraire(id, jour, arriveeEntreprise, retourDomicile);
    }
}
