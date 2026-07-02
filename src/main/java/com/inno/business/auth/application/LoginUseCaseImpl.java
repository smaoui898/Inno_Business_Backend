package com.inno.business.auth.application;

import com.inno.business.auth.domain.exception.InvalidCredentialsException;
import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.in.LoginUseCase;
import com.inno.business.auth.domain.port.out.PasswordVerifierPort;
import com.inno.business.auth.domain.port.out.TokenGeneratorPort;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;

public class LoginUseCaseImpl implements LoginUseCase {

    private final UserRepositoryPort userRepository;
    private final TokenGeneratorPort tokenGenerator;
    private final PasswordVerifierPort passwordVerifier;

    public LoginUseCaseImpl(UserRepositoryPort userRepository, TokenGeneratorPort tokenGenerator, PasswordVerifierPort passwordVerifier) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordVerifier = passwordVerifier;
    }

    @Override
    public LoginResult execute(LoginCommand command) {
        // 1. Chercher l'utilisateur 
        User user = userRepository.findByEmail(command.email()).orElseThrow(InvalidCredentialsException::new);

        // 2. VÃ©rifier le mot de passe
        if (!passwordVerifier.matches(command.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }
        //3.Ge©ne©ration du token
        String token = tokenGenerator.generate(user);

        return new LoginResult(token, user.getEmailValue(), user.getRole(),user.getPrenom(),user.getNom()); //resultat
    }

}
