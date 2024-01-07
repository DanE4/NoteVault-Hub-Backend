package com.dane.notevault.dto;

import com.dane.notevault.entity.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.UUID;

public record UserDTO(

        UUID id,
        @NotBlank
        @Size(min = 3, max = 30)
        String username,
        @Email
        String email,
        @Valid
        @NotNull
        Role role) implements Serializable {

}