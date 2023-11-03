package com.dane.homework_help.dto;

import java.util.List;
import java.util.UUID;

public record PostDTO(UUID id, String title, String content, UserDTO user, List<PostToSubjectDTO> subjects,
                      List<FileDTO> files) {
}
