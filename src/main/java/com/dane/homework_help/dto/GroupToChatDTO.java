package com.dane.homework_help.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.GroupToChat}
 */
public record GroupToChatDTO(UUID id, GroupDTO group, ChatDTO chat) implements Serializable {
}