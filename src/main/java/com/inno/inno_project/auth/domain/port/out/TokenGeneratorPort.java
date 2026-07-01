package com.inno.inno_project.auth.domain.port.out;

import com.inno.inno_project.auth.domain.model.User;

public interface TokenGeneratorPort {

    String generate(User user);
}
