package com.speedtype.seed;

import com.speedtype.model.Role;
import com.speedtype.model.User;
import com.speedtype.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_PASSWORD = "admin";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(DEFAULT_ADMIN_USERNAME)) {
            return;
        }

        User admin = new User();
        admin.setUsername(DEFAULT_ADMIN_USERNAME);
        admin.setPassword(passwordEncoder.encode(DEFAULT_ADMIN_PASSWORD));
        admin.setRole(Role.ADMIN);
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);

        System.out.println("=======================================================");
        System.out.println(" Default admin account created:");
        System.out.println("   username: " + DEFAULT_ADMIN_USERNAME);
        System.out.println("   password: " + DEFAULT_ADMIN_PASSWORD);
        System.out.println(" This only prints once. Log in and change it — see README.");
        System.out.println("=======================================================");
    }
}
