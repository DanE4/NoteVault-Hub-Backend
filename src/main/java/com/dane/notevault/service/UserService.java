package com.dane.notevault.service;

import com.dane.notevault.auth.Response;
import com.dane.notevault.auth.request.RegisterRequest;
import com.dane.notevault.auth.request.RegisterResponse;
import com.dane.notevault.dto.UserDTO;
import com.dane.notevault.entity.User;
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

    int enableAppUser(String email);

    RegisterResponse registerUser(RegisterRequest request);
}
