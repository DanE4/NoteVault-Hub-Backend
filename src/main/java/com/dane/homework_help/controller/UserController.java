package com.dane.homework_help.controller;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.exception.MissingJwtException;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.mapper.UserMapper;
import com.dane.homework_help.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@Tag(name = "User")
public class UserController {

    //?why do i need to use this? it's just dependency injection, right?

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public String extractJwtFromReqest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        jwt = authHeader.substring(7);
        log.info("jwt: " + jwt);
        return jwt;
    }

    @Operation(
            description = "Get endpoint for users",
            summary = "This is a summary for management get endpoint",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> getUserById(@PathVariable(value = "user_id") UUID id, HttpServletRequest request) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder().data(userMapper.apply(userService.getUserById(id))).build());
        } catch (UsernameNotFoundException | MissingJwtException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @Operation(
            description = "Endpoint for getting all users",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping()
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        return ResponseEntity.ok().body(Response.builder()
                .data(userService.getAllUsers().stream().map(userMapper).toList()).build());
    }

    /*
    @PostMapping()
    public User createUser(@RequestBody User user) {
        log.info("create user: " + user);
        return userRepository.save(user);
    }
     */

    @Operation(
            description = "Endpoint for updating user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @PatchMapping("/{user_id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> updateUser(@PathVariable(value = "user_id") UUID id,
                                               HttpServletRequest request, @RequestBody UserDTO user) {
        try {
            return ResponseEntity.ok().body(userService.updateUser(id, user, extractJwtFromReqest(request)));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (MissingJwtException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @Operation(
            description = "Endpoint for deleting user",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{user_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> deleteUser(@PathVariable(value = "user_id") UUID id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok().build();
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @Value("${application.security.jwt.secret-key}")
    private String jwtSecret;

    @GetMapping("/jwt-secret")
    public String getJwtSecret() {
        log.warn("get jwt secret");
        return jwtSecret;
    }

    /*
    @PostMapping("/email")
    public ResponseEntity<Response> getUserByEmail(@RequestBody String email) {
        User user = userRepository.findByUsername(email);
        log.warn("user: " + user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok().body(Response.builder().data(user).build());
    }
    */
}