package com.inno.inno_project.auth.infrastructure.adapter.out.security;

import com.inno.inno_project.auth.infrastructure.adapter.out.persistence.SpringUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Utilise SpringUserRepository directement â€” les deux sont infrastructure
@Service
public class UserDetailsServiceAdapter implements UserDetailsService {

    private final SpringUserRepository springUserRepository;

    public UserDetailsServiceAdapter(SpringUserRepository springUserRepository) {
        this.springUserRepository = springUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return springUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvÃ© : " + email));
    }
}
