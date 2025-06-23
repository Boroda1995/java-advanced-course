package com.galdovich.security.service;

import com.galdovich.security.entity.UserEntity;
import com.galdovich.security.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DataInitializer {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @PostConstruct
    public void init() {
        createUserIfNotExists("user@example.com", "password", Set.of("VIEW_INFO"));
        createUserIfNotExists("admin@example.com", "adminpass", Set.of("VIEW_INFO", "VIEW_ADMIN"));
        createUserIfNotExists("restricted@example.com", "restricted", Set.of());
    }

    private void createUserIfNotExists(String email, String password, Set<String> permissions) {
        if (!userRepo.existsByEmail(email)) {
            UserEntity user = new UserEntity();
            user.setEmail(email);
            user.setPassword(encoder.encode(password));
            user.setPermissions(permissions);
            userRepo.save(user);
        }
    }
}
