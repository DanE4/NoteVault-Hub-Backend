package com.dane.homework_help.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
public record PostDTO(
        @Schema(hidden = true)
        UUID id,
        @Schema(description = "title", example = "This is my new post")
        String title,
        @Schema(description = "content", example = "lorem ipsum")
        String content,
        @Schema(hidden = true)
        UUID userId,
        @Schema(hidden = true)
        List<UUID> subjectIds,
        @Schema(hidden = true)
        List<UUID> fileIds)
        implements Serializable {
}
