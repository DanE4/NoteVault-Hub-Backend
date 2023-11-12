package com.dane.homework_help.service.impl;

import com.dane.homework_help.auth.RegisterRequest;
import com.dane.homework_help.auth.Response;
import com.dane.homework_help.auth.service.JwtService;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.exception.UserNotFoundException;
import com.dane.homework_help.mapper.UserMapper;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public User createUser(RegisterRequest userDTO) {
        User user = User.builder()
                .email(userDTO.email())
                .username(userDTO.username())
                .role(userDTO.role())
                .build();
        return userRepository.save(user);
    }


    public User CheckIfAuthorized(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User extractedUser = userRepository.findByEmail(email);

        if (extractedUser == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        if (extractedUser.getId() != id && extractedUser.getAuthorities()
                .stream()
                .noneMatch(a -> a.toString().equals("ADMIN"))) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        return extractedUser;
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getUserById(UUID id) {
        CheckIfAuthorized(id);
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
    }

    @Override
    @CachePut(value = "users", key = "#id")
    public Response updateUser(UUID id, UserDTO userDTO, String jwt) {

        User extractedUser = jwtService.getUserByJwt(jwt);
        this.authorize(id, extractedUser);
        var user = userRepository.findById(id).orElseThrow();

        if (userDTO.email() != null) {
            user.setEmail(userDTO.email());
        }
        if (userDTO.role() != null) {
            user.setRole(userDTO.role());
        }

        jwtService.revokeAllUserTokens(user);
        if (extractedUser.getAuthorities().stream().noneMatch(a -> a.toString().equals("ADMIN")) ||
                (extractedUser.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.toString().equals("ADMIN")) && id == extractedUser.getId())) {
            return Response.builder()
                    .data(Map.of(
                            "user", userMapper.apply(userRepository.save(user)),
                            "accessToken", jwtService.generateToken(user),
                            "refreshToken", jwtService.generateRefreshToken(user)
                    ))
                    .build();
        }
        return Response.builder()
                .data(Map.of(
                        "user", userMapper.apply(userRepository.save(user))
                ))
                .build();
    }

    @Override
    public void deleteUserById(UUID id) {
        log.info("delete user by id: " + id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + "not be found"));
        userRepository.delete(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper)
                .toList();
    }

    public void authorize(UUID id, User extractedUser) {
        if (id != extractedUser.getId() && extractedUser.getAuthorities()
                .stream()
                .noneMatch(a -> a.toString().equals("ADMIN"))) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
    }

}