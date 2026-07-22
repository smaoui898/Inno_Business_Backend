package com.inno.business.management.companie.application;

import java.util.List;

import com.inno.business.auth.domain.model.User;
import com.inno.business.auth.domain.port.out.UserRepositoryPort;
import com.inno.business.management.companie.domain.model.Societe;
import com.inno.business.management.companie.domain.ports.in.GetSocietesUseCase;
import com.inno.business.management.companie.domain.ports.out.SocieteRepositoryPort;

public class GetSocietesUseCaseImpl implements GetSocietesUseCase {
    private final UserRepositoryPort userRepository;
    private final SocieteRepositoryPort societeRepository;

    public GetSocietesUseCaseImpl(UserRepositoryPort userRepository, SocieteRepositoryPort societeRepository) {
        this.userRepository = userRepository;
        this.societeRepository = societeRepository;
    }

   @Override
    public List<Societe> execute(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // ROLE_MANAGER → ne voit que les sociétés qui lui sont assignées
        if ("ROLE_MANAGER".equals(user.getRole())) {
            return societeRepository.findAllByManagerId(user.getId());
        }

        // ROLE_USER (owner) → voit toutes ses sociétés
        return societeRepository.findAllByOwnerId(user.getId());
    }
}
