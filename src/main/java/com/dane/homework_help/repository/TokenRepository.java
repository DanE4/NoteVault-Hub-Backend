package com.dane.homework_help.repository;

import com.dane.homework_help.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);

    List<Token> findAllValidTokenByUserId(UUID id);
}
