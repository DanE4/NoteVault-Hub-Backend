package com.dane.homework_help.dto;

import com.dane.homework_help.entity.enums.FileExtension;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.File}
 */
public record FileDTO(UUID id,
                      @NotBlank
                      FileExtension fileExtension,
                      String path,
                      @NotBlank
                      UserDTO uploader, PostDTO post,
                      ChatDTO chat) implements Serializable {
}