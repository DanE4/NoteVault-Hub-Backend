package com.dane.homework_help.dto;

import com.dane.homework_help.entity.enums.FileExtension;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.File}
 */
public record FileDTO(UUID id, FileExtension fileExtension, String path, UserDTO uploader, PostDTO post,
                      ChatDTO chat) implements Serializable {
}