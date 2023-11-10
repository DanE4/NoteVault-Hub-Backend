package com.dane.homework_help.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.Chat}
 */

public record ChatDTO(UUID id, List<UUID> chatToUserIds, List<UUID> groupToChatIds,
                      List<UUID> messageIds,
                      List<UUID> fileIds) implements Serializable {
}
