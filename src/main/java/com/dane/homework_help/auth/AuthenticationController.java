package com.dane.homework_help.auth;

import com.dane.homework_help.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthenticationController {

    //login
    //register
    //logout
    //forgot password
    //reset password
    //change password
    //change email
    //using bcrypt, jwt, and spring security, and mail sending
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthenticationService authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<Response> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request)); //
    }

    @Operation(
            description = "Register endpoint for users",
            summary = "This is a summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody RegisterRequest request) {
        logger.warn("register");
        var response = authenticationService.register(request);
        logger.warn(response.toString());
        if (response.response == null) {
            return ResponseEntity.ok(response);
        } else if (response.response.equals("Email already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else if (response.response.equals("Username already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(
            description = "Token refresh endpoint for users (should be used when access token is expired, by client)",
            summary = "This is a summary for refreshing tokens",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
