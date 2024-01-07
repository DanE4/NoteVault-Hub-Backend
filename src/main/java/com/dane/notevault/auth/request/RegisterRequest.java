package com.dane.notevault.auth.request;

import com.dane.notevault.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder

public record RegisterRequest(

        @Pattern(regexp = "^[a-zA-Z0-9]{3,20}$", message = "Username must be between 3 and 20 characters long and contain only letters and numbers")
        @Schema(description = "Username", example = "johndoe")
        String username,

        @Email(message = "Invalid email")
        @Schema(description = "Email", example = "johndoe@example.com")
        String email,

        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit, and be at least 8 characters long")
        @Schema(description = "Password", example = "password123")
        String password,

        @Schema(description = "Role", example = "USER")
        Role role
) {
}
