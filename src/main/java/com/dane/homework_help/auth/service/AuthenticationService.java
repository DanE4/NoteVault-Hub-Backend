package com.dane.homework_help.auth.service;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.auth.request.AuthenticationRequest;
import com.dane.homework_help.exception.EmailNotConfirmedException;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private EmailValidator emailValidator;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Response authenticate(AuthenticationRequest request) {
        try {

            log.info("Authenticating");
            var asd = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = userRepository.findByEmail(request.getEmail());
            var accessToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            jwtService.saveUserToken(user, accessToken);
            jwtService.saveUserToken(user, refreshToken);
            return Response.builder()
                    .data(Map.of(
                            "accessToken", accessToken,
                            "refreshToken", refreshToken
                    ))
                    .build();
        } catch (EmailNotConfirmedException e) {
            log.error("User's email is not confirmed");
            return Response.builder()
                    .response("User's email is not confirmed")
                    .build();
        } catch (UsernameNotFoundException e) {
            log.error("User not found");
            return Response.builder()
                    .response("User not found")
                    .build();
        } catch (Exception e) {
            log.error("Error authenticating user: {}", e.getMessage());
            return Response.builder()
                    .response("Error authenticating user")
                    .build();
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return;
        }
        String jwt = authorizationHeader.substring(7);
        String email = jwtService.extractEmail(jwt);
        if (email != null) {
            var user = this.userRepository.findByEmail(email);
            if (jwtService.validateToken(jwt, user)) {
                var accessToken = jwtService.generateToken(user);
                jwtService.revokeAllUserTokens(user);
                jwtService.saveUserToken(user, accessToken);
                var authResponse = Response.builder()
                        .data(
                                Map.of(
                                        "accessToken", accessToken,
                                        "refreshToken", jwt
                                )
                        )
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
