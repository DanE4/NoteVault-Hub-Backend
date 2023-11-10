package com.dane.homework_help.service;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.UserDTO;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(UUID id);

    @CachePut(value = "users", key = "#id")
    Response updateUser(UUID id, UserDTO userDTO, String jwt);

    void deleteUserById(UUID id);

    List<UserDTO> getAllUsers();
}
