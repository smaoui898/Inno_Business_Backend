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
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringCompanyRepository;
import com.inno.business.management.beneficiaire.application.CreateBeneficiaireUseCaseImpl;
import com.inno.business.management.beneficiaire.application.DeleteBeneficiaireUseCaseImpl;
import com.inno.business.management.beneficiaire.application.ExportBeneficiairesUseCaseImpl;
import com.inno.business.management.beneficiaire.application.GetBeneficiairesUseCaseImpl;
import com.inno.business.management.beneficiaire.application.ImportBeneficiairesUseCaseImpl;
import com.inno.business.management.beneficiaire.application.UpdateBeneficiaireUseCaseImpl;
import com.inno.business.management.beneficiaire.domain.port.in.CreateBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.DeleteBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.ExportBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.GetBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.ImportBeneficiairesUseCase;
import com.inno.business.management.beneficiaire.domain.port.in.UpdateBeneficiaireUseCase;
import com.inno.business.management.beneficiaire.domain.port.out.BeneficiaireRepositoryPort;
import com.inno.business.management.companie.application.AssignManagerUseCaseImpl;
import com.inno.business.management.companie.application.CreateManagerUseCaseImpl;
import com.inno.business.management.companie.application.CreateSocieteUseCaseImpl;
import com.inno.business.management.companie.application.DeleteManagerUseCaseImpl;
import com.inno.business.management.companie.application.DeleteSocieteUseCaseImpl;
import com.inno.business.management.companie.application.GetSocietesUseCaseImpl;
import com.inno.business.management.companie.application.UpdateManagerUseCaseImpl;
import com.inno.business.management.companie.application.UpdateSocieteUseCaseImpl;
import com.inno.business.management.companie.domain.ports.in.AssignManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.CreateManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.CreateSocieteUseCase;
import com.inno.business.management.companie.domain.ports.in.DeleteManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.DeleteSocieteUseCase;
import com.inno.business.management.companie.domain.ports.in.GetSocietesUseCase;
import com.inno.business.management.companie.domain.ports.in.UpdateManagerUseCase;
import com.inno.business.management.companie.domain.ports.in.UpdateSocieteUseCase;
import com.inno.business.management.companie.domain.ports.out.ManagerRepositoryPort;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;
import com.inno.business.management.sites.application.CreateSiteUseCaseImpl;
import com.inno.business.management.sites.application.DeleteSiteUseCaseImpl;
import com.inno.business.management.sites.application.GetSitesUseCaseImpl;
import com.inno.business.management.sites.application.UpdateSiteUseCaseImpl;
import com.inno.business.management.sites.domain.port.in.CreateSiteUseCase;
import com.inno.business.management.sites.domain.port.in.DeleteSiteUseCase;
import com.inno.business.management.sites.domain.port.in.GetSitesUseCase;
import com.inno.business.management.sites.domain.port.in.UpdateSiteUseCase;
import com.inno.business.management.sites.domain.port.out.SiteRepositoryPort;

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

    @Bean
    public DeleteManagerUseCase deleteManagerUseCase(UserRepositoryPort userRepository,
            ManagerRepositoryPort managerRepository,
            SocieteRepositoryPort societeRepository) {
        return new DeleteManagerUseCaseImpl(userRepository, managerRepository, societeRepository);
    }

    // Sites 
    @Bean
    public GetSitesUseCase getSitesUseCase(UserRepositoryPort u, SiteRepositoryPort s,
            SpringCompanyRepository c) {
        return new GetSitesUseCaseImpl(u, s, c);
    }

    @Bean
    public CreateSiteUseCase createSiteUseCase(UserRepositoryPort u, SiteRepositoryPort s,
            PasswordEncoderPort p, SpringCompanyRepository c) {
        return new CreateSiteUseCaseImpl(u, s, p, c);
    }

    @Bean
    public UpdateSiteUseCase updateSiteUseCase(UserRepositoryPort u, SiteRepositoryPort s,
            SpringCompanyRepository c) {
        return new UpdateSiteUseCaseImpl(u, s, c);
    }

    @Bean
    public DeleteSiteUseCase deleteSiteUseCase(UserRepositoryPort u, SiteRepositoryPort s,
            SpringCompanyRepository c) {
        return new DeleteSiteUseCaseImpl(u, s, c);
    }
    //  Bénéficiaires 

    @Bean
    public GetBeneficiairesUseCase getBeneficiairesUseCase(UserRepositoryPort u,
            BeneficiaireRepositoryPort b,
            SpringCompanyRepository c) {
        return new GetBeneficiairesUseCaseImpl(u, b, c);
    }

    @Bean
    public CreateBeneficiaireUseCase createBeneficiaireUseCase(UserRepositoryPort u,
            SiteRepositoryPort s,
            BeneficiaireRepositoryPort b,
            SpringCompanyRepository c) {
        return new CreateBeneficiaireUseCaseImpl(u, s, b, c);
    }

    @Bean
    public UpdateBeneficiaireUseCase updateBeneficiaireUseCase(UserRepositoryPort u,
            BeneficiaireRepositoryPort b,
            SpringCompanyRepository c) {
        return new UpdateBeneficiaireUseCaseImpl(u, b, c);
    }

    @Bean
    public DeleteBeneficiaireUseCase deleteBeneficiaireUseCase(UserRepositoryPort u,
            BeneficiaireRepositoryPort b,
            SpringCompanyRepository c) {
        return new DeleteBeneficiaireUseCaseImpl(u, b, c);
    }

    @Bean
    public ImportBeneficiairesUseCase importBeneficiairesUseCase(UserRepositoryPort u,
            SiteRepositoryPort s,
            BeneficiaireRepositoryPort b,
            SpringCompanyRepository c) {
        return new ImportBeneficiairesUseCaseImpl(u, s, b, c);
    }

    @Bean
    public ExportBeneficiairesUseCase exportBeneficiairesUseCase(UserRepositoryPort u,
            BeneficiaireRepositoryPort b,
            SpringCompanyRepository c) {
        return new ExportBeneficiairesUseCaseImpl(u, b, c);
    }

}
