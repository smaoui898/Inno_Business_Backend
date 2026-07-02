package com.inno.business.auth.domain.port.out;

public interface PasswordVerifierPort {
    boolean matches(String rawPassword, String hashedPassword); //raw : mdp en clair, hashed : mdp hashÃ©
    
}
