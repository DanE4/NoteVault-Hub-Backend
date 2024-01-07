package com.dane.notevault.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.dane.notevault.entity.Message}
 */
public record MessageDTO(UUID id,
                         @NotBlank
                         String message, LocalDateTime createdAt, ChatDTO chat, UserDTO user,
                         boolean isRead) implements Serializable {
}