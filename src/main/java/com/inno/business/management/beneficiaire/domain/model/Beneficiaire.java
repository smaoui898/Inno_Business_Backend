package com.inno.business.management.beneficiaire.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Beneficiaire {
    
    private final UUID               id;
    private final UUID               siteId;
    private final UUID               societeId;
    private final String             prenom;
    private final String             nom;
    private final String             email;
    private final String             telephone;
    private final String             cin;
    private final String             matricule;
    private final List<Adresse>      adresses;
    private final TypeCourse         typeCourse;
    private final StatutBeneficiaire statut;
    private final UUID               activiteId;  // nullable
    private final UUID               shiftId;     // nullable
    private final LocalDateTime      createdAt;

    public Beneficiaire(UUID id, UUID siteId, UUID societeId,
                        String prenom, String nom, String email,
                        String telephone, String cin, String matricule,
                        List<Adresse> adresses, TypeCourse typeCourse,
                        StatutBeneficiaire statut, UUID activiteId,
                        UUID shiftId, LocalDateTime createdAt) {
        if (siteId == null) throw new IllegalArgumentException("siteId obligatoire");
        if (prenom == null || prenom.isBlank())
            throw new IllegalArgumentException("Le prénom est obligatoire");
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom est obligatoire");
        if (telephone == null || telephone.isBlank())
            throw new IllegalArgumentException("Le téléphone est obligatoire");

        this.id          = id;
        this.siteId      = siteId;
        this.societeId   = societeId;
        this.prenom      = prenom;
        this.nom         = nom;
        this.email       = email;
        this.telephone   = telephone;
        this.cin         = cin;
        this.matricule   = matricule;
        this.adresses    = adresses != null ? adresses : new ArrayList<>();
        this.typeCourse  = typeCourse != null ? typeCourse : TypeCourse.STANDARD;
        this.statut      = statut != null ? statut : StatutBeneficiaire.ACTIF;
        this.activiteId  = activiteId;
        this.shiftId     = shiftId;
        this.createdAt   = createdAt;
    }

    public UUID               getId()         { return id; }
    public UUID               getSiteId()     { return siteId; }
    public UUID               getSocieteId()  { return societeId; }
    public String             getPrenom()     { return prenom; }
    public String             getNom()        { return nom; }
    public String             getEmail()      { return email; }
    public String             getTelephone()  { return telephone; }
    public String             getCin()        { return cin; }
    public String             getMatricule()  { return matricule; }
    public List<Adresse>      getAdresses()   { return adresses; }
    public TypeCourse         getTypeCourse() { return typeCourse; }
    public StatutBeneficiaire getStatut()     { return statut; }
    public UUID               getActiviteId() { return activiteId; }
    public UUID               getShiftId()    { return shiftId; }
    public LocalDateTime      getCreatedAt()  { return createdAt; }
}

