package com.dane.homework_help.dto;

import com.dane.homework_help.entity.enums.FileExtension;

/**
 * DTO for {@link com.dane.homework_help.entity.File}
 */
public record FileDTO(String id, FileExtension fileExtension, String path, UserDTO uploader, PostDTO post,
                      ChatDTO chat) {
}