package com.usermanagement.api;

import com.usermanagement.api.config.RootConfig;
import com.usermanagement.api.repository.UserRepository;
import com.usermanagement.api.service.HelloService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import  com.usermanagement.api.entity.Role;
import  com.usermanagement.api.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class Main {
    public static void main(String[] args) {
        var context = new AnnotationConfigApplicationContext(RootConfig.class);
        PasswordEncoder encoder = context.getBean(PasswordEncoder.class);

        String hash = encoder.encode("secret123");
        System.out.println("Hash: " + hash);
        System.out.println("Matches right? " + encoder.matches("secret123", hash));
        System.out.println("Matches wrong? " + encoder.matches("wrong", hash));
        System.out.println("Hash again: " + encoder.encode("secret123"));
        context.close();
    }
}