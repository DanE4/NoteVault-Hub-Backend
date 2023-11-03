package com.dane.homework_help.api.service;

import com.dane.homework_help.auth.service.impl.JwtServiceImpl;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.exception.UnauthorizedException;
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

import java.util.ArrayList;
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

        userDTO = UserDTO.builder()
                .email("random@gmail.com")
                .password("$2a$12$sTJkWVSZRfAO/CzQC1fsaeXbNc1bQ21RAWTIwnWU70OrUyCXzii12")
                .username("random")
                .role(Role.ADMIN)
                .build();


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
    public void getUserById_AuthorizedUser_ShouldReturnUserDTO() {
        // Mock the behavior of JwtService to return a User when getUserByJwt is called with a valid JWT
        when(jwtService.getUserByJwt(jwt)).thenReturn(userWithAdminRole);
        // Mock the behavior of UserRepository to return a User when findById is called with userId
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithAdminRole));
        // Act
        UserDTO result = userService.getUserById(userId, jwt);
        // Assert
        Assertions.assertThat(result).isNotNull();
    }

    @Test
    public void getUserById_UnauthorizedUser_ShouldReturnUnauthorizedException() {
        //Act
        when(jwtService.getUserByJwt(jwt)).thenReturn(userWithUserRole);
        // Assert
        Assertions.assertThatThrownBy(() -> userService.getUserById(userId, jwt))
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
