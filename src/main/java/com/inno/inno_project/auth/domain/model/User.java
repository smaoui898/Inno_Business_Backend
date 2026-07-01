package com.inno.inno_project.auth.domain.model;


import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.inno_project.auth.domain.valueobject.Email;

public class User {

    private final UUID          id;
    private final String        prenom;
    private final String        nom;
    private final String        telephone;
    private final Email         email;
    private final String        password;
    private final String        nomGroupe;
    private final String        raisonSociale;
    private final String        identifiantUnique;
    private final String        adresse;
    private final String        ville;
    private final String        emailSociete;
    private final String        telephoneSociete;
    private final String        rneDocumentPath;
    private final String        patenteDocumentPath;
    private final String        cinGerantDocumentPath;
    private final String        role;
    private final LocalDateTime createdAt;
    private LocalDateTime lastLogin;   

    public User(UUID id, String prenom, String nom, String telephone,
                Email email, String password, String nomGroupe,
                String raisonSociale, String identifiantUnique,
                String adresse, String ville, String emailSociete,
                String telephoneSociete, String rneDocumentPath,
                String patenteDocumentPath, String cinGerantDocumentPath,
                String role, LocalDateTime createdAt, LocalDateTime lastLogin) {

        if (prenom == null || prenom.isBlank())
            throw new IllegalArgumentException("Le prénom est obligatoire");
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom est obligatoire");
        if (password == null || password.isBlank())
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        if (role == null || role.isBlank())
            throw new IllegalArgumentException("Le rôle est obligatoire");

        this.id                    = id;
        this.prenom                = prenom;
        this.nom                   = nom;
        this.telephone             = telephone;
        this.email                 = email;
        this.password              = password;
        this.nomGroupe             = nomGroupe;
        this.raisonSociale         = raisonSociale;
        this.identifiantUnique     = identifiantUnique;
        this.adresse               = adresse;
        this.ville                 = ville;
        this.emailSociete          = emailSociete;
        this.telephoneSociete      = telephoneSociete;
        this.rneDocumentPath       = rneDocumentPath;
        this.patenteDocumentPath   = patenteDocumentPath;
        this.cinGerantDocumentPath = cinGerantDocumentPath;
        this.role                  = role;
        this.createdAt             = createdAt;
        this.lastLogin             = lastLogin;
    }

    // Méthode domaine — enregistre le moment du login
    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public UUID          getId()                    { return id; }
    public String        getPrenom()                { return prenom; }
    public String        getNom()                   { return nom; }
    public String        getTelephone()             { return telephone; }
    public String        getEmailValue()            { return email.value(); }
    public String        getPassword()              { return password; }
    public String        getNomGroupe()             { return nomGroupe; }
    public String        getRaisonSociale()         { return raisonSociale; }
    public String        getIdentifiantUnique()     { return identifiantUnique; }
    public String        getAdresse()               { return adresse; }
    public String        getVille()                 { return ville; }
    public String        getEmailSociete()          { return emailSociete; }
    public String        getTelephoneSociete()      { return telephoneSociete; }
    public String        getRneDocumentPath()       { return rneDocumentPath; }
    public String        getPatenteDocumentPath()   { return patenteDocumentPath; }
    public String        getCinGerantDocumentPath() { return cinGerantDocumentPath; }
    public String        getRole()                  { return role; }
    public LocalDateTime getCreatedAt()             { return createdAt; }
    public LocalDateTime getLastLogin()             { return lastLogin; }
}