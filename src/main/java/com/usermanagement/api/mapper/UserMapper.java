package com.usermanagement.api.mapper;

import com.usermanagement.api.dto.*;
import com.usermanagement.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // password is NOT set here: the service hashes it first
    public User toEntity(UserCreateRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setRole(request.role());
        return user;
    }

    public void updateEntity(User user, UserUpdateRequest request) {
        user.setName(request.name());
        user.setEmail(request.email());
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}