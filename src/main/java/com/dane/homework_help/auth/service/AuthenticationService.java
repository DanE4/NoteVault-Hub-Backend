package com.dane.homework_help.auth.service;

import com.dane.homework_help.auth.AuthenticationRequest;
import com.dane.homework_help.auth.RegisterRequest;
import com.dane.homework_help.auth.Response;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.repository.TokenRepository;
import com.dane.homework_help.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Response authenticate(AuthenticationRequest request) {
        log.info("authenticating");
        authenticationManager.authenticate(
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
    }

    public Response register(RegisterRequest request) {
        logger.info("registering");
        try {
            if (userRepository.findByEmail(request.email()) != null) {
                logger.warn("Email already exists");
                return Response.builder()
                        .response("Email already exists")
                        .build();
            } else if (userRepository.existsByUsername(request.username())) {
                logger.warn("Username already exists");
                return Response.builder()
                        .response("Username already exists")
                        .build();
            }
            //register
            var user = User.builder()
                    .username(request.username())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .role(Role.USER)
                    .build();
            try {
                userRepository.save(user);
            } catch (ConstraintViolationException e) {
                logger.error(e.getMessage());
                return Response.builder()
                        .response("Invalid input")
                        .build();
            }
            //create and return jwt
            logger.info("User registered");
            return Response.builder()
                    .data(Map.of(
                            "accessToken", jwtService.generateToken(user),
                            "refreshToken", jwtService.generateRefreshToken(user)
                    ))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Response.builder()
                    .response("Internal server error")
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
