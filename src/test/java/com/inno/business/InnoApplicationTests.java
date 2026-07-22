package com.inno.business;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.inno.business.auth.infrastructure.adapter.out.persistence.SpringUserRepository;

@SpringBootTest
class InnoApplicationTests {

    @Autowired
    private SpringUserRepository userRepo;

    @Test
    void contextLoads() {
        System.out.println("=== DIAGNOSTIC FOR EYA PASSWORD ===");
        org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder encoder = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
        String hash = "$2a$10$wYmSQIr7z94lFpab.HB4p.0JMiOtAi1oYmxwy.LZVodrGky/kih9.";
        String[] candidates = {
            "password", "password123", "eya", "eya@gmail.com", "eya123", "123456", "12345678", "admin", "manager", "manager123"
        };
        for (String cand : candidates) {
            System.out.println("Candidate '" + cand + "': " + encoder.matches(cand, hash));
        }
        System.out.println("===================================");
    }

}
