package com.dane.homework_help.dto;

import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.GroupToChat}
 */
public record GroupToChatDTO(UUID id, GroupDTO group, ChatDTO chat) {
}