package com.dane.homework_help.dto;

import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.Chat}
 */

public record ChatDTO(UUID id, List<ChatToUserDTO> users, List<GroupToChatDTO> groups, List<MessageDTO> messages,
                      List<FileDTO> files) {
}
