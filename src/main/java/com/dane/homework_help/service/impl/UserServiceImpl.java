package com.dane.homework_help.service.impl;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.auth.service.JwtService;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.exception.UserNotFoundException;
import com.dane.homework_help.mapper.UserMapper;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RedisTemplate<String, Object> redisTemplate;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository, JwtService jwtService,
                           RedisTemplate<String, Object> redisTemplate) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = User.builder()
                .email(userDTO.email())
                .password(userDTO.password())
                .username(userDTO.username())
                .role(userDTO.role())
                .build();
        User newUser = userRepository.save(user);
        return new UserDTO(newUser.getId(), newUser.getUsername(), newUser.getEmail(),
                newUser.getPassword(), newUser.getRole());
    }

    /*
        @Override
        public UserDTO getUserById(int id, String jwt) {
            var extractedUser = jwtService.getUserByJwt(jwt);
            this.authorize(id, extractedUser);

            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + "not be found"));
            return userDTOMapper.apply(user);
        }*/


    @Override
    public UserDTO getUserById(int id) {
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

        UserDTO user = (UserDTO) redisTemplate.opsForValue().get("users::" + id);

        if (user == null) {
            log.info("Fetching user from DB");
            User userFromDb = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id " + id + " not found"));
            user = userMapper.apply(userFromDb);
            redisTemplate.opsForValue().set("users::" + id, user);
        } else {
            log.info("User fetched from cache");
        }

        return user;
    }


    @Override
    @CachePut(value = "users", key = "#id")
    public Response updateUser(int id, UserDTO userDTO, String jwt) {

        User extractedUser = jwtService.getUserByJwt(jwt);
        this.authorize(id, extractedUser);
        var user = userRepository.findById(id).orElseThrow();

        if (userDTO.password() != null) {
            user.setPassword(userDTO.password());
        }
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
    public void deleteUserById(int id) {
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

    public void authorize(int id, User extractedUser) {
        if (id != extractedUser.getId() && extractedUser.getAuthorities()
                .stream()
                .noneMatch(a -> a.toString().equals("ADMIN"))) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
    }

}