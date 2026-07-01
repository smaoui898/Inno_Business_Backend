package com.inno.inno_project.auth.infrastructure.config;

import com.inno.inno_project.auth.application.LoginUseCaseImpl;
import com.inno.inno_project.auth.domain.port.in.LoginUseCase;
import com.inno.inno_project.auth.domain.port.out.PasswordVerifierPort;
import com.inno.inno_project.auth.domain.port.out.TokenGeneratorPort;
import com.inno.inno_project.auth.domain.port.out.UserRepositoryPort;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.inno.inno_project.auth.application.RegisterUseCaseImpl;
import com.inno.inno_project.auth.domain.port.in.RegisterUseCase;
import com.inno.inno_project.auth.domain.port.out.FileStoragePort;
import com.inno.inno_project.auth.domain.port.out.PasswordEncoderPort;


@Configuration
public class BeanConfig {

    @Bean
    public LoginUseCase loginUseCase(UserRepositoryPort userRepository,
            TokenGeneratorPort tokenGenerator,
            PasswordVerifierPort passwordVerifier) {
        return new LoginUseCaseImpl(userRepository, tokenGenerator, passwordVerifier);
    }
    @Bean
    public RegisterUseCase registerUseCase(UserRepositoryPort userRepository,PasswordEncoderPort passwordEncoder, FileStoragePort fileStorage){
        return new RegisterUseCaseImpl(userRepository, passwordEncoder, fileStorage);
    }
}
