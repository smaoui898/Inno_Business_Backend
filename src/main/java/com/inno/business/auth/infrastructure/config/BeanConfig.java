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
import com.inno.business.management.companie.application.AssignManagerUseCaseImpl;
import com.inno.business.management.companie.application.CreateManagerUseCaseImpl;
import com.inno.business.management.companie.application.CreateSocieteUseCaseImpl;
import com.inno.business.management.companie.application.DeleteSocieteUseCaseImpl;
import com.inno.business.management.companie.application.GetSocietesUseCaseImpl;
import com.inno.business.management.companie.application.UpdateManagerUseCaseImpl;
import com.inno.business.management.companie.application.UpdateSocieteUseCaseImpl;
import com.inno.business.management.companie.domain.ports.in.AssignManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.CreateManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.CreateSocieteUseCase;
import com.inno.business.management.companie.domain.ports.in.DeleteSocieteUseCase;
import com.inno.business.management.companie.domain.ports.in.GetSocietesUseCase;
import com.inno.business.management.companie.domain.ports.in.UpdateManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.UpdateSocieteUseCase;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

@Configuration
public class BeanConfig {

    @Bean
    public LoginUseCase loginUseCase(UserRepositoryPort userRepository,
            TokenGeneratorPort tokenGenerator,
            PasswordVerifierPort passwordVerifier) {
        return new LoginUseCaseImpl(userRepository, tokenGenerator, passwordVerifier);
    }

    @Bean
    public RegisterUseCase registerUseCase(UserRepositoryPort userRepository, CompanyRepositoryPort companyRepository, PasswordEncoderPort passwordEncoder, FileStoragePort fileStorage) {
        return new RegisterUseCaseImpl(userRepository, companyRepository, passwordEncoder, fileStorage);
    }

    @Bean
    public GetSocietesUseCase getSocietesUseCase(UserRepositoryPort userRepository,
            SocieteRepositoryPort societeRepository) {
        return new GetSocietesUseCaseImpl(userRepository, societeRepository);
    }

    @Bean
    public CreateSocieteUseCase createSocieteUseCase(UserRepositoryPort userRepository,
            SocieteRepositoryPort societeRepository,
            FileStoragePort fileStorage) {
        return new CreateSocieteUseCaseImpl(userRepository, societeRepository, fileStorage);
    }

    @Bean
    public UpdateSocieteUseCase updateSocieteUseCase(UserRepositoryPort userRepository,
            SocieteRepositoryPort societeRepository) {
        return new UpdateSocieteUseCaseImpl(userRepository, societeRepository);
    }

    @Bean
    public DeleteSocieteUseCase deleteSocieteUseCase(UserRepositoryPort userRepository,
            SocieteRepositoryPort societeRepository) {
        return new DeleteSocieteUseCaseImpl(userRepository, societeRepository);
    }

    @Bean
    public CreateManagerUseCase createManagerUseCase(UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            SocieteRepositoryPort societeRepository,
            ManagerRepositoryPort managerRepository) {
        return new CreateManagerUseCaseImpl(
                userRepository, passwordEncoder, societeRepository, managerRepository);
    }

    @Bean
    public AssignManagerUseCase assignManagerUseCase(UserRepositoryPort userRepository,
            SocieteRepositoryPort societeRepository) {
        return new AssignManagerUseCaseImpl(userRepository, societeRepository);
    }

    @Bean
    public UpdateManagerUseCase updateManagerUseCase(UserRepositoryPort userRepository,
            ManagerRepositoryPort managerRepository,
            SocieteRepositoryPort societeRepository) {
        return new UpdateManagerUseCaseImpl(userRepository, managerRepository, societeRepository);
    }
}
