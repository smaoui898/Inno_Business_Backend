package com.inno.business.management.shift.infrastructure.adapter.out.persistence;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.inno.business.auth.infrastructure.adapter.out.persistence.UserJpaEntity;
import com.inno.business.management.shift.domain.model.Shift;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shifts")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class ShiftJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // FK vers l'owner (group owner)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserJpaEntity owner;

    @Column(nullable = false)
    private String nom;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Cascade ALL + orphanRemoval : remplacer la liste supprime les anciens horaires
    //shift 1 <--- N shift_horaire
    @OneToMany(mappedBy = "shift", cascade = CascadeType.ALL,
               orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ShiftHoraireJpaEntity> horaires = new ArrayList<>();

    // Mapping JPA → Domain
    public Shift toDomain() {
        return new Shift(
                id,
                owner.getId(),
                nom,
                horaires.stream().map(ShiftHoraireJpaEntity::toDomain).toList(),
                createdAt
        );
    }
}
