package com.dane.homework_help.dto;

import com.dane.homework_help.entity.File;
import com.dane.homework_help.entity.PostToSubject;

import java.util.List;
import java.util.UUID;

public record PostDTO(UUID id, String title, String content, UserDTO user, List<PostToSubject> subjects,
                      List<File> files) {
}
