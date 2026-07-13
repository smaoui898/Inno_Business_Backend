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
    public List<Societe> execute(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        return societeRepository.findAllByOwnerId(owner.getId());
}
}
