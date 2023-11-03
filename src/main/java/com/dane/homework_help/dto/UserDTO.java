package com.dane.homework_help.dto;

import com.dane.homework_help.entity.enums.Role;

public record UserDTO(int id, String username, String email, String password, Role role) {
}