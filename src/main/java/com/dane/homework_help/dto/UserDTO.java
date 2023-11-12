package com.dane.homework_help.dto;

import com.dane.homework_help.entity.enums.Role;

import java.io.Serializable;
import java.util.UUID;

public record UserDTO(UUID id, String username, String email,
                      Role role) implements Serializable {

}