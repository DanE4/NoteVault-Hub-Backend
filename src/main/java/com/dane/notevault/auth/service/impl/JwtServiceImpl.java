package com.dane.notevault.auth.service.impl;

import com.dane.notevault.auth.service.JwtService;
import com.dane.notevault.entity.Token;
import com.dane.notevault.entity.User;
import com.dane.notevault.entity.enums.TokenType;
import com.dane.notevault.repository.TokenRepository;
import com.dane.notevault.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long accessTokenExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshTokenExpiration;

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    public String extractEmail(String jwt) {
        return extractClaim(jwt, Claims::getSubject);
    }

    @Override
    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(Map.of("id", userRepository.findByEmail(userDetails.getUsername()).getId()), userDetails);
    }

    @Override
    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String jwt, UserDetails userDetails) {
        final String email = extractEmail(jwt);
        /*
        var token = tokenRepository.findByToken(jwt).orElseThrow(() -> new MissingJwtException("Token not found"));
        if (token.isRevoked() || token.isExpired()) {
            throw new InvalidTokenException("Token is invalid");
        }
         */
        return email.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    @Override
    public boolean isTokenExpired(String jwt) {
        return extractExpiration(jwt).before(new Date());
    }

    @Override
    public Date extractExpiration(String jwt) {
        return extractClaim(jwt, Claims::getExpiration);
    }

    @Override
    public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwt);
        return claimsResolver.apply(claims);
    }

    @Override
    public Claims extractAllClaims(String jwt) {
        //log.info(jwt);
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody();
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        return generateRefreshToken(Map.of("id", userRepository.findByEmail(userDetails.getUsername())
                .getId()), userDetails);
    }

    @Override
    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public UUID extractUserId(String jwt) {
        return extractClaim(jwt, claims -> (UUID) claims.get("id"));
    }

    @Override
    public User getUserByJwt(String jwt) {

        return userRepository.findById(extractUserId(jwt)).orElseThrow();
    }

    @Override
    public void saveUserToken(User user, String jwt) {
        var token = Token.builder()
                .user(user)
                .token(jwt)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    @Override
    public void revokeAllUserTokens(User user) {
        log.info("Revoking all user tokens");
        var validUserTokens = tokenRepository.findAllValidTokenByUserId(user.getId());
        if (validUserTokens.isEmpty()) {
            log.info("No valid tokens found");
            return;
        }
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
        log.info("Revoked all user tokens");
    }

    @Override
    public String getSecretKey() {
        return secretKey;
    }

}
