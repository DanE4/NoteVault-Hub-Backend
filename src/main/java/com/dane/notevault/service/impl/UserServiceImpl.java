package com.dane.notevault.service.impl;

import com.dane.notevault.auth.Response;
import com.dane.notevault.auth.model.ConfirmationToken;
import com.dane.notevault.auth.request.RegisterRequest;
import com.dane.notevault.auth.request.RegisterResponse;
import com.dane.notevault.auth.service.AuthZService;
import com.dane.notevault.auth.service.JwtService;
import com.dane.notevault.dto.UserDTO;
import com.dane.notevault.entity.User;
import com.dane.notevault.entity.enums.Role;
import com.dane.notevault.exception.UnauthorizedException;
import com.dane.notevault.exception.UserNotFoundException;
import com.dane.notevault.mapper.UserMapper;
import com.dane.notevault.repository.ConfirmationTokenRepository;
import com.dane.notevault.repository.UserRepository;
import com.dane.notevault.service.UserService;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final AuthZService authZService;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmationTokenRepository confirmationTokenRepository;


    @Override
    public User createUser(RegisterRequest userDTO) {
        User user = User.builder()
                .email(userDTO.email())
                .username(userDTO.username())
                .role(userDTO.role())
                .build();
        return userRepository.save(user);
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public User getUserById(UUID id) {
        authZService.CheckIfAuthorized(id);
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
    @Cacheable(value = "users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void authorize(UUID id, User extractedUser) {
        if (id != extractedUser.getId() && extractedUser.getAuthorities()
                .stream()
                .noneMatch(a -> a.toString().equals("ADMIN"))) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
    }

    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);
    }

    public RegisterResponse registerUser(RegisterRequest request) {
        try {
            if (userRepository.findByEmail(request.email()) != null) {
                log.warn("Email already exists");
                return RegisterResponse.builder().error("Email already exists").build();
            } else if (userRepository.existsByUsername(request.username())) {
                log.warn("Username already exists");
                return RegisterResponse.builder().error("Username already exists").build();
            }

            var user = User.builder()
                    .username(request.username())
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .role(Role.USER)
                    .build();
            userRepository.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    UUID.randomUUID().toString(),
                    user,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15));

            log.info("Saving token");
            confirmationTokenRepository.save(confirmationToken);
            log.info("Token saved");
            log.info(confirmationToken.toString());

            return new RegisterResponse(
                    jwtService.generateToken(user),
                    jwtService.generateRefreshToken(user),
                    confirmationToken.getToken(),
                    ""
            );
        } catch (ConstraintViolationException e) {
            log.error(e.getMessage());
            return RegisterResponse.builder().error("Invalid input").build();
        }
    }
}