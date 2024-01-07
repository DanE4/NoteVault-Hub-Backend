package com.dane.notevault.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Email(message = "Invalid email")
    @Schema(description = "Email", example = "admn4testing1234+random@gmail.com")
    private String email;

    @Schema(description = "Password", example = "sogoodpassword")
    //pattern should be only that  0-10 length
    //@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit, and be at least 8 characters long")
    //?This is only for testing, production should contain the line above
    private String password;
}
