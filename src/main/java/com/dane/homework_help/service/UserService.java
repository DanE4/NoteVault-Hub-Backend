package com.dane.homework_help.service;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO createUser(UserDTO userDTO);

    UserDTO getUserById(int id);

    Response updateUser(int id, UserDTO userDTO, String jwt);

    void deleteUserById(int id);

    List<UserDTO> getAllUsers();
}
