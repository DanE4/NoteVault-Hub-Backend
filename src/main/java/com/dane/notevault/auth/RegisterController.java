package com.dane.notevault.auth;

import com.dane.notevault.auth.request.RegisterRequest;
import com.dane.notevault.auth.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Service
@RestController
@RequiredArgsConstructor
@Tag(name = "Register")
@RequestMapping("/api/register")
public class RegisterController {
    private final RegisterService registrationService;

    @Operation(
            description = "Register endpoint for users, the token is sent to the user's email after registration, the" +
                    " token is valid for 15 minutes, the credentials are validated before registration",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400"
                    ),
            }
    )
    @PostMapping("/")
    public ResponseEntity<Response> register(@RequestBody @Valid RegisterRequest request) {
        log.warn("register");
        var response = registrationService.register(request);
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
            description = "Confirm endpoint for users, the token is sent to the user's email after registration, the token is valid for 15 minutes",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Validation Error",
                            responseCode = "400"
                    ),
            }
    )
    @PostMapping(path = "confirm")
    public ResponseEntity<Response> confirm(@RequestParam("token") String token) {
        try {
            log.warn("Confirming...");
            return ResponseEntity.ok(registrationService.confirmToken(token));
        } catch (IllegalStateException e) {
            log.error("Token not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder()
                    .response("Token not found")
                    .build());
        } catch (Exception e) {
            log.error("Error confirming token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Response.builder()
                    .response("Error confirming token")
                    .build());
        }
    }

}
