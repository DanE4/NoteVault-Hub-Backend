package com.dane.homework_help.dto;

import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.ChatToUser}
 */
public record ChatToUserDTO(UUID id, ChatDTO chat, UserDTO user) {
}