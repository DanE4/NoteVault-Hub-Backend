package com.dane.homework_help.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Schema(description = "Email", example = "admn4testing1234+random@gmail.com")
    private String email;
    @Schema(description = "Password", example = "sogoodpassword")
    private String password;
}
