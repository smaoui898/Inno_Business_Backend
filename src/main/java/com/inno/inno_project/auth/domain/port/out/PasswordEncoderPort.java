package com.inno.inno_project.auth.domain.port.out;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}