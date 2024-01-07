package com.dane.notevault.dto;

import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.notevault.entity.Group}
 */
public record GroupDTO(UUID id,
                       @Size(min = 2, max = 30)
                       String name,
                       @Size(min = 2, max = 200)
                       String description) implements Serializable {


}