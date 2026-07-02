package com.inno.business.auth.domain.port.out;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}