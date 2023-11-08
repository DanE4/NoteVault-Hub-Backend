package com.dane.homework_help.dto;

import com.dane.homework_help.entity.enums.Role;

import java.io.Serializable;

public record UserDTO(int id, String username, String email, String password, Role role) implements Serializable {

}