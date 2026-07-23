package com.inno.business.auth.application;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.in.GetCurrentUserUseCase;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;

public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    private final UserRepositoryPort userRepository;

    public GetCurrentUserUseCaseImpl(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User execute(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}