package com.dane.notevault.controller;

import com.dane.notevault.auth.Response;
import com.dane.notevault.dto.UserDTO;
import com.dane.notevault.exception.MissingJwtException;
import com.dane.notevault.exception.UnauthorizedException;
import com.dane.notevault.mapper.UserMapper;
import com.dane.notevault.service.UserService;
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
@Tag(name = "User")
@RequestMapping("/api/users")
public class UserController {
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
            description = "Endpoint for getting user by id",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserDTO.class)
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    )
            }
    )
    @GetMapping("/{user_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> getUserById(@PathVariable(value = "user_id") UUID id) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder().data(userMapper.apply(userService.getUserById(id))).build());
        } catch (UsernameNotFoundException | MissingJwtException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().response("User not found").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        }
    }

    //TODO: need to return a list of users in the example 200 response
    @Operation(
            description = "Endpoint for getting all users",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserDTO.class)
                            )
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
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserDTO.class)
                            )
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        } catch (MissingJwtException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder().response("Missing JWT").build());
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().response("User not found").build());
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