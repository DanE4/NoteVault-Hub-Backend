package com.dane.homework_help.dto;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public record PostDTO(UUID id, String title, String content, UUID userId, List<UUID> subjectIds,
                      List<UUID> fileIds) implements Serializable {
}
