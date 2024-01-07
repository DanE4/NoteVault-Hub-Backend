package com.dane.notevault.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
public record PostDTO(
        @Schema(hidden = true)
        UUID id,
        @Schema(description = "title", example = "This is my new post")
        @Size(min = 2, max = 30)
        String title,
        @Schema(description = "content", example = "lorem ipsum")
        @Size(min = 2, max = 30)
        String content,
        @Schema(hidden = true)
        UUID userId,
        List<UUID> subjectIds,
        List<UUID> fileIds)
        implements Serializable {
}
