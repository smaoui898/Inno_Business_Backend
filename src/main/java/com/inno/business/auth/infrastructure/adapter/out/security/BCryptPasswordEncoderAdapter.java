// infrastructure/auth/security/BCryptPasswordEncoderAdapter.java
package com.inno.business.auth.infrastructure.adapter.out.security;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.inno.business.auth.domain.port.out.PasswordEncoderPort;

@Component
public class BCryptPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder encoder;

    public BCryptPasswordEncoderAdapter(PasswordEncoder encoder) { this.encoder = encoder; }

    @Override
    public String encode(String raw) { return encoder.encode(raw); }
}