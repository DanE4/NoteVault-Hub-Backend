package com.dane.homework_help.service;

import com.dane.homework_help.auth.RegisterRequest;
import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    User createUser(RegisterRequest userDTO);

    User getUserById(UUID id);

    Response updateUser(UUID id, UserDTO userDTO, String jwt);

    void deleteUserById(UUID id);

    List<User> getAllUsers();
}
