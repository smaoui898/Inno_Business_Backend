package com.inno.business.auth.domain.port.in;

public interface LoginUseCase {

    record LoginCommand(String email, String password) {}

    record LoginResult(String token, String refreshToken, String email, String role,String prenom,String nom) {}

    // methode qui execute la commande de login et retourne le resultat
    LoginResult execute(LoginCommand command);
}
