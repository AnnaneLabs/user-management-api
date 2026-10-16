package com.usermanagement.api.service;

import com.usermanagement.api.entity.Role;
import com.usermanagement.api.entity.User;
import com.usermanagement.api.repository.UserRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.existsByEmail("admin@test.com")) {
            return;                       // already there, do nothing
        }
        User admin = new User();
        admin.setName("Admin");
        admin.setEmail("admin@test.com");
        admin.setPassword(passwordEncoder.encode("admin12345"));
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
    }
}