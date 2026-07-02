package com.inno.business.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.inno.business.auth.domain.valueobject.Email;

public class User {

    private final UUID id;
    private final String prenom;
    private final String nom;
    private final String telephone;
    private final Email email;
    private final String password;
    private final String role;
    private final LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User(UUID id, String prenom, String nom, String telephone, Email email, String password, String role, LocalDateTime createdAt, LocalDateTime lastLogin) {

        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Le prénom est obligatoire");
        }
        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Le mot de passe est obligatoire");
        }
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Le rôle est obligatoire");
        }

        this.id = id;
        this.prenom = prenom;
        this.nom = nom;
        this.telephone = telephone;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNom() {
        return nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmailValue() {
        return email.value();
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
}
