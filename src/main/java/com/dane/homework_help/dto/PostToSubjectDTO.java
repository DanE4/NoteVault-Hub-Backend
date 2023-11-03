package com.dane.homework_help.dto;

import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.PostToSubject}
 */
public record PostToSubjectDTO(UUID id, PostDTO post, SubjectDTO subject) {
}