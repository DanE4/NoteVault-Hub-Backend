package com.dane.notevault.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.dane.notevault.entity.Chat}
 */

public record ChatDTO(
        @Schema(hidden = true)
        UUID id,
        @Schema(description = "title", example = "This is my new chat")
        @Size(min = 2, max = 30)
        String title,
        List<UUID> chatToUserIds,
        List<UUID> groupToChatIds,
        List<UUID> messageIds,
        List<UUID> fileIds)
        implements Serializable {
}
