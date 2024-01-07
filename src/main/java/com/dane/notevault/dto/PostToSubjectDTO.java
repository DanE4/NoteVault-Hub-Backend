package com.dane.notevault.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.dane.notevault.entity.PostToSubject}
 */
public record PostToSubjectDTO(UUID id, PostDTO post, SubjectDTO subject) implements Serializable {
}