package com.inno.inno_project.auth.infrastructure.adapter.out.persistence;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.inno.inno_project.auth.domain.model.User;
import com.inno.inno_project.auth.domain.valueobject.Email;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class UserJpaEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String prenom;
    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String telephone;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "nom_groupe")
    private String nomGroupe;

    @Column(name = "raison_sociale")
    private String raisonSociale;

    @Column(name = "identifiant_unique")
    private String identifiantUnique;

    private String adresse;
    private String ville;

    @Column(name = "email_societe")
    private String emailSociete;

    @Column(name = "telephone_societe")
    private String telephoneSociete;

    @Column(name = "rne_document_path")
    private String rneDocumentPath;

    @Column(name = "patente_document_path")
    private String patenteDocumentPath;

    @Column(name = "cin_gerant_document_path")
    private String cinGerantDocumentPath;

    @Column(nullable = false)
    private String role;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    // ── Mapping : Domain → JPA ────────────────────────────────────────
    public static UserJpaEntity fromDomain(User u) {
        UserJpaEntity e = new UserJpaEntity();
        e.id                    = u.getId();
        e.prenom                = u.getPrenom();
        e.nom                   = u.getNom();
        e.telephone             = u.getTelephone();
        e.email                 = u.getEmailValue();
        e.password              = u.getPassword();
        e.nomGroupe             = u.getNomGroupe();
        e.raisonSociale         = u.getRaisonSociale();
        e.identifiantUnique     = u.getIdentifiantUnique();
        e.adresse               = u.getAdresse();
        e.ville                 = u.getVille();
        e.emailSociete          = u.getEmailSociete();
        e.telephoneSociete      = u.getTelephoneSociete();
        e.rneDocumentPath       = u.getRneDocumentPath();
        e.patenteDocumentPath   = u.getPatenteDocumentPath();
        e.cinGerantDocumentPath = u.getCinGerantDocumentPath();
        e.role                  = u.getRole();
        e.createdAt             = u.getCreatedAt();
        e.lastLogin             = u.getLastLogin();
        return e;
    }

    // ── Mapping : JPA → Domain ────────────────────────────────────────
    public User toDomain() {
        return new User(
                id, prenom, nom, telephone, new Email(email),
                password, nomGroupe, raisonSociale, identifiantUnique,
                adresse, ville, emailSociete, telephoneSociete,
                rneDocumentPath, patenteDocumentPath, cinGerantDocumentPath,
                role, createdAt, lastLogin
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override 
    public String  getUsername()    { 
        return email; 
    }
    @Override 
    public boolean isAccountNonExpired()     { 
        return true; 
    }
    @Override 
    public boolean isAccountNonLocked()      { 
        return true; 
    }
    @Override 
    public boolean isCredentialsNonExpired() { 
        return true; 
    }
    @Override 
    public boolean isEnabled()    { 
        return true; 
    }
}