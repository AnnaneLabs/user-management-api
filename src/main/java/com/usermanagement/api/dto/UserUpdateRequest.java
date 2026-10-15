package com.usermanagement.api.dto;

import jakarta.validation.constraints.*;

public record UserUpdateRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        String email
) {}