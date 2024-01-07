package com.dane.notevault.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.notevault.entity.GroupToChat}
 */
public record GroupToChatDTO(UUID id, GroupDTO group, ChatDTO chat) implements Serializable {
}