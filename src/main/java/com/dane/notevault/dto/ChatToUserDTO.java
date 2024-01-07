package com.dane.notevault.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.notevault.entity.ChatToUser}
 */
public record ChatToUserDTO(UUID id, ChatDTO chat, UserDTO user) implements Serializable {
}