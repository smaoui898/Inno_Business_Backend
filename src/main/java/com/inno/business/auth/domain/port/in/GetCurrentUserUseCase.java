package com.inno.business.auth.domain.port.in;

import com.inno.business.auth.domain.model.User;

public interface GetCurrentUserUseCase {
    // à partir de l'email (extrait du token/cookie), retrouve l'utilisateur
    User execute(String email);
}