package com.inno.business.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.inno.business.auth.application.LoginUseCaseImpl;
import com.inno.business.auth.application.RegisterUseCaseImpl;
import com.inno.business.auth.domain.port.in.LoginUseCase;
import com.inno.business.auth.domain.port.in.RegisterUseCase;
import com.inno.business.auth.domain.port.out.CompanyRepositoryPort;
import com.inno.business.auth.domain.port.out.FileStoragePort;
import com.inno.business.auth.domain.port.out.PasswordEncoderPort;
import com.inno.business.auth.domain.port.out.PasswordVerifierPort;
import com.inno.business.auth.domain.port.out.TokenGeneratorPort;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;


@Configuration
public class BeanConfig {

    @Bean
    public LoginUseCase loginUseCase(UserRepositoryPort userRepository,
            TokenGeneratorPort tokenGenerator,
            PasswordVerifierPort passwordVerifier) {
        return new LoginUseCaseImpl(userRepository, tokenGenerator, passwordVerifier);
    }
    @Bean
    public RegisterUseCase registerUseCase(UserRepositoryPort userRepository,CompanyRepositoryPort companyRepository,PasswordEncoderPort passwordEncoder, FileStoragePort fileStorage){
        return new RegisterUseCaseImpl(userRepository,companyRepository, passwordEncoder, fileStorage);
    }
}
