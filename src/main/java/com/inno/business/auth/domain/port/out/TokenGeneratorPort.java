package com.inno.business.auth.domain.port.out;

import com.inno.business.auth.domain.model.User;

public interface TokenGeneratorPort {

    String generate(User user);
    String generateRefresh(User user);
}
