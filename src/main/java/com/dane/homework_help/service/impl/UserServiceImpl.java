package com.dane.homework_help.service.impl;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.auth.service.JwtService;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.exception.UserNotFoundException;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .username(userDTO.getUsername())
                .role(userDTO.getRole())
                .build();
        User newUser = userRepository.save(user);
        return UserDTO.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .username(newUser.getUsername())
                .role(newUser.getRole())
                .build();
    }

    /*
        @Override
        public UserDTO getUserById(int id, String jwt) {
            var extractedUser = jwtService.getUserByJwt(jwt);
            this.authorize(id, extractedUser);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + "not be found"));
            return mapToDto(user);
        }*/
    @Override
    public UserDTO getUserById(int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User extractedUser = userRepository.findByUsername(email);

        if (extractedUser == null) {
            // Handle the case where no user with the given username is found
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        if (extractedUser.getId() != id && extractedUser.getAuthorities()
                .stream()
                .noneMatch(a -> a.toString().equals("ADMIN"))) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
        return mapToDto(user);
    }


    @Override
    public Response updateUser(int id, UserDTO userDTO, String jwt) {

        User extractedUser = jwtService.getUserByJwt(jwt);
        this.authorize(id, extractedUser);
        var user = userRepository.findById(id).orElseThrow();

        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getRole() != null) {
            user.setRole(userDTO.getRole());
        }

        jwtService.revokeAllUserTokens(user);
        if (extractedUser.getAuthorities().stream().noneMatch(a -> a.toString().equals("ADMIN")) ||
                (extractedUser.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.toString().equals("ADMIN")) && id == extractedUser.getId())) {

            return Response.builder()
                    .data(Map.of(
                            "user", mapToDto(userRepository.save(user)),
                            "accessToken", jwtService.generateToken(user),
                            "refreshToken", jwtService.generateRefreshToken(user)
                    ))
                    .build();
        }
        return Response.builder()
                .data(Map.of(
                        "user", mapToDto(userRepository.save(user))
                ))
                .build();
    }

    @Override
    public void deleteUserById(int id) {
        log.info("delete user by id: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + "not be found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDto).toList();
    }

    private UserDTO mapToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    private User mapToEntity(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .username(userDTO.getUsername())
                .role(userDTO.getRole())
                .build();
    }

    public void authorize(int id, User extractedUser) {
        if (id != extractedUser.getId() && extractedUser.getAuthorities()
                .stream()
                .noneMatch(a -> a.toString().equals("ADMIN"))) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
    }

}