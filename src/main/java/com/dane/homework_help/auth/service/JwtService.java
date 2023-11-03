package com.dane.homework_help.auth.service;

import com.dane.homework_help.entity.User;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    String extractEmail(String jwt);

    Key getSigningKey();

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> claims, UserDetails userDetails);

    boolean validateToken(String jwt, UserDetails userDetails);

    boolean isTokenExpired(String jwt);

    Date extractExpiration(String jwt);

    <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver);

    Claims extractAllClaims(String jwt);

    String generateRefreshToken(UserDetails userDetails);

    String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails);

    int extractUserId(String jwt);

    User getUserByJwt(String jwt);

    void saveUserToken(User user, String jwt);

    void revokeAllUserTokens(User user);

    String getSecretKey();

}