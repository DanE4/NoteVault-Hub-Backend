package com.dane.homework_help.api.service;

import com.dane.homework_help.auth.service.impl.JwtServiceImpl;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.mapper.UserMapper;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.impl.UserServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

//using mockito
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private JwtServiceImpl jwtService;
    private UserMapper userMapper;

    private User userWithAdminRole;
    private User userWithUserRole;
    private UserDTO userDTO;
    private List<User> mockedUsers;
    private final int userId = 1;
    private final String jwt = "secretToken";

    @BeforeEach
    public void init() {
        //Arrange
        userWithAdminRole = User.builder()
                .email("random@gmail.com")
                .password("$2a$12$sTJkWVSZRfAO/CzQC1fsaeXbNc1bQ21RAWTIwnWU70OrUyCXzii12")
                .username("random")
                .role(Role.ADMIN)
                .build();

        userWithUserRole = User.builder()
                .email("random@gmail.com")
                .password("$2a$12$sTJkWVSZRfAO/CzQC1fsaeXbNc1bQ21RAWTIwnWU70OrUyCXzii12")
                .username("random")
                .role(Role.USER)
                .build();

        userDTO = new UserDTO(
                1,
                "random",
                "random@gmail.com",
                "$2a$12$sTJkWVSZRfAO/CzQC1fsaeXbNc1bQ21RAWTIwnWU70OrUyCXzii12",
                Role.ADMIN);


        mockedUsers = new ArrayList<>();

        mockedUsers.add(User.builder().email("random@gmail.com")
                .password("$2a$12$sTJkWVSZRfAO/CzQC1fsaeXbNc1bQ21RAWTIwnWU70OrUyCXzii12")
                .username("random")
                .role(Role.ADMIN)
                .build());

        mockedUsers.add(User.builder().email("random2@gmail.com")
                .password("$2a$12$sTJkWVSZRfAO/CzQC1fsaeXbNc1bQ21RAWTIwnWU70OrUyCXzii13")
                .username("random2")
                .role(Role.ADMIN)
                .build());

    }


    @Test
    public void createUser_ShouldCreateUser() {
        //Act
        when(userRepository.save(Mockito.any(User.class))).thenReturn(userWithAdminRole);
        //Assert
        UserDTO savedUser = userService.createUser(userDTO);
        Assertions.assertThat(savedUser).isNotNull();
    }

    @Test
    public void getAllUsers_ShouldReturnAllUsersDTOs() {
        // Mock the behavior of userRepository.findAll()
        when(userRepository.findAll()).thenReturn(mockedUsers);
        // Call the service method
        List<UserDTO> userDTOs = userService.getAllUsers();
        // Assertions
        assertEquals(mockedUsers.size(), userDTOs.size());
        // Add more assertions to compare the UserDTOs with the mock data
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getUserById_AuthorizedUser_ShouldReturnUserDTO() {
        // Mock the behavior of UserRepository to return a User when findById is called with userId
        when(userRepository.findByUsername(userWithAdminRole.getEmail())).thenReturn(userWithAdminRole);
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithAdminRole));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userWithAdminRole.getUsername())
                .password(userWithAdminRole.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(userWithAdminRole.getRole().name())))
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "password");

        // Set the authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        UserDTO result = userService.getUserById(userId);

        // Assert
        Assertions.assertThat(result).isNotNull();
    }


    @Test
    public void getUserById_UnauthorizedUser_ShouldReturnUnauthorizedException() {
        //Act
        when(userRepository.findByUsername(userWithUserRole.getEmail())).thenReturn(userWithUserRole);
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userWithUserRole.getUsername())
                .password(userWithUserRole.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(userWithUserRole.getRole().name())))
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Assert
        Assertions.assertThatThrownBy(() -> userService.getUserById(userId + 1))
                .isInstanceOf(UnauthorizedException.class);
    }
    //updateUser
    //!Needs implement

    //deleteUser
    @Test
    public void deleteUserById_ShouldDeleteUser() {
        //Act
        when(userRepository.findById(userWithAdminRole.getId())).thenReturn(Optional.of(userWithAdminRole));
        //Assert
        assertAll(() -> userService.deleteUserById(userWithAdminRole.getId()));
    }
}
