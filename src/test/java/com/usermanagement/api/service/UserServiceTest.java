package com.usermanagement.api.service;

import com.usermanagement.api.dto.UserCreateRequest;
import com.usermanagement.api.dto.UserResponse;
import com.usermanagement.api.entity.Role;
import com.usermanagement.api.entity.User;
import com.usermanagement.api.exception.EmailAlreadyUsedException;
import com.usermanagement.api.mapper.UserMapper;
import com.usermanagement.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;

    @Test
    void create_throwsWhenEmailAlreadyUsed() {
        // Arrange: the fake repository says "this email exists"
        when(userRepository.existsByEmail("a@test.com")).thenReturn(true);
        UserService service = new UserService(userRepository, new UserMapper(), passwordEncoder);

        // Act + Assert
        assertThrows(EmailAlreadyUsedException.class, () ->
                service.create(new UserCreateRequest("Alice", "a@test.com", "password123", Role.USER)));

        // Nothing must have been saved
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_hashesPasswordAndNeverReturnsIt() {
        when(userRepository.existsByEmail("a@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("HASHED");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        UserService service = new UserService(userRepository, new UserMapper(), passwordEncoder);

        UserResponse response = service.create(
                new UserCreateRequest("Alice", "a@test.com", "password123", Role.USER));

        assertEquals("a@test.com", response.email());
        verify(passwordEncoder).encode("password123");   // the password was hashed
    }
}