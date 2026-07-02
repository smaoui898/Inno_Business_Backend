package com.inno.business.auth.infrastructure.adapter.out.security;

import org.springframework.stereotype.Component;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.TokenGeneratorPort;
import com.inno.business.auth.infrastructure.security.JwtService;

@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

    private final JwtService jwtService;

    public JwtTokenAdapter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generate(User user) {
        return jwtService.generateToken(user.getEmailValue());
    }
}
