package com.dane.homework_help.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.Group}
 */
public record GroupDTO(UUID id, String name, String description) implements Serializable {
}