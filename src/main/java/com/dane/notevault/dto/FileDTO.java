package com.dane.notevault.dto;

import com.dane.notevault.entity.enums.FileExtension;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.notevault.entity.File}
 */
public record FileDTO(UUID id,
                      @NotBlank
                      FileExtension fileExtension,
                      String path,
                      @NotBlank
                      UserDTO uploader, PostDTO post,
                      ChatDTO chat) implements Serializable {
}