package com.dane.homework_help.auth;

import com.dane.homework_help.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder

public record RegisterRequest(
        @Schema(description = "Username", example = "johndoe")
        String username,
        @Schema(description = "Email", example = "johndoe@example.com")
        String email,
        @Schema(description = "Password", example = "password123")
        String password,
        Role role
) {
}
