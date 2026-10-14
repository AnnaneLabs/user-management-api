package com.usermanagement.api;

import com.usermanagement.api.config.RootConfig;
import com.usermanagement.api.repository.UserRepository;
import com.usermanagement.api.service.HelloService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import  com.usermanagement.api.entity.Role;
import  com.usermanagement.api.entity.User;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(RootConfig.class);
        UserRepository repo = context.getBean(UserRepository.class);

        User u = new User();
        u.setName("Alice");
        u.setEmail("alice@test.com");
        u.setPassword("temp");
        u.setRole(Role.ADMIN);
        repo.save(u);

        System.out.println("Saved id=" + u.getId() + ", createdAt=" + u.getCreatedAt());
        System.out.println("Exists email? " + repo.existsByEmail("alice@test.com"));
        System.out.println("Active users: " + repo.findAllByActiveTrue().size());

        u.setActive(false);
        repo.save(u);
        System.out.println("Active after deactivation: " + repo.findAllByActiveTrue().size());
        context.close();
    }
}