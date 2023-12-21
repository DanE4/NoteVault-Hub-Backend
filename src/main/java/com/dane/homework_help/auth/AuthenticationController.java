package com.dane.homework_help.auth;

import com.dane.homework_help.auth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
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
    private final AuthenticationService authenticationService;
    @GetMapping("/loginForm")
    public String showLoginForm() {
        return "loginForm";
    }
    @PostMapping("/authenticate")
    public ResponseEntity<Response> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {

        log.warn("login");
        Response authResponse = authenticationService.authenticate(request);
        Map<String, String> data = (Map<String, String>) authResponse.getData();

        Cookie cookie = new Cookie("token", data.get("accessToken"));
        cookie.setPath("/"); // Allows frontend to access the cookie
        cookie.setHttpOnly(true); // Makes the cookie HTTP-only
        cookie.setMaxAge(24 * 60 * 60); // Sets the cookie to expire in 1 day

        Cookie cookie2 = new Cookie("refreshToken", data.get("refreshToken"));
        cookie2.setPath("/"); // Allows frontend to access the cookie
        cookie2.setHttpOnly(true); // Makes the cookie HTTP-only
        cookie2.setMaxAge(30 * 24 * 60 * 60); // Sets the cookie to expire in 30 days
        // cookie.setSecure(true); // Uncomment this line if you're using HTTPS

        response.addCookie(cookie);
        response.addCookie(cookie2);
        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/registerForm")
    public String showRegisterForm() {
        return "registerForm";
    }

    @Operation(
            description = "Register endpoint for users",
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
        log.warn("register");
        var response = authenticationService.register(request);
        log.warn(response.toString());
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
