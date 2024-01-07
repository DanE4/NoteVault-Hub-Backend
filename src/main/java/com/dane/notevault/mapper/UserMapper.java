package com.dane.notevault.mapper;

import com.dane.notevault.dto.UserDTO;
import com.dane.notevault.entity.User;
import org.springframework.stereotype.Service;

import java.util.function.Function;


@Service
public class UserMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(),
                user.getRole());
    }

    public User mapToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.id())
                .username(userDTO.username())
                .email(userDTO.email())
                .role(userDTO.role())
                .build();
    }
}
