package com.dane.homework_help.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.Subject}
 */
public record SubjectDTO(
        @Schema(hidden = true)
        UUID id,

        @Schema(description = "name", example = "Calculus 2")
        String name,

        @Schema(description = "description", example = "Best math class")
        String description,

        @Schema(hidden = true)
        List<UUID> postsToSubjectIds) implements Serializable {
}