package com.dane.homework_help.mapper;

import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO mapToDto(User user);

    List<UserDTO> mapToDtos(List<User> users);

    @Mapping(target = "userDto.id", source = "id")
    @Mapping(target = "userDto.username", source = "username")
    @Mapping(target = "userDto.email", source = "email")
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    User signUpToUser(UserDTO userDto);
}

/*
@Service
public class UserDTOMapper implements Function<User, UserDTO> {
    @Override
    public UserDTO apply(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail(),
                user.getPassword(), user.getRole());
    }

    public User mapToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.id())
                .username(userDTO.username())
                .email(userDTO.email())
                .password(userDTO.password())
                .role(userDTO.role())
                .build();
    }
}
*/