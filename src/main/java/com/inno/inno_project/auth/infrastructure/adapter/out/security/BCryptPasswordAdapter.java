package com.inno.inno_project.auth.infrastructure.adapter.out.security;

import com.inno.inno_project.auth.domain.port.out.PasswordVerifierPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordAdapter implements PasswordVerifierPort {

    private final PasswordEncoder passwordEncoder;

    public BCryptPasswordAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean matches(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
