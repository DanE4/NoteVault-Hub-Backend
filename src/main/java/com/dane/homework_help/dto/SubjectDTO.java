package com.dane.homework_help.dto;

import com.dane.homework_help.entity.PostToSubject;

import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.dane.homework_help.entity.Subject}
 */
public record SubjectDTO(UUID id, String name, String description, List<PostToSubject> posts) {
}