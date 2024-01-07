package com.dane.homework_help.api.service;

import com.dane.homework_help.auth.request.RegisterRequest;
import com.dane.homework_help.auth.service.AuthZService;
import com.dane.homework_help.dto.UserDTO;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

//using mockito
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;
    private User userWithAdminRole;
    private User userWithUserRole;
    private UserDTO userDTO;
    private RegisterRequest createRequest;
    private List<User> mockedUsers;
    private final UUID userId = UUID.randomUUID();
    private final String jwt = "secretToken";
    @Mock
    private AuthZService authZService;

    @BeforeEach
    public void setup() {
        //Arrange
        userWithAdminRole = User.builder()
                .id(userId)
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

        createRequest = new RegisterRequest(
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
        User savedUser = userService.createUser(createRequest);
        assertEquals(userWithAdminRole, savedUser);
    }

    @Test
    public void getAllUsers_ShouldReturnAllUsers() {
        // Mock the behavior of userRepository.findAll()
        when(userRepository.findAll()).thenReturn(mockedUsers);
        // Call the service method
        List<User> userDTOs = userService.getAllUsers();
        // Assertions
        assertEquals(mockedUsers.size(), userDTOs.size());
        // Add more assertions to compare the UserDTOs with the mock data
    }

    public static String solution(String message) {
        String[] splitted = message.split("");
        Map<String, Integer> map = new HashMap<>();
        for (int i = 1; i < splitted.length; i++) {
            if (map.get(splitted[i]) == null) {
                map.put(splitted[i], 1);
            } else if (map.get(splitted[i]) != null) {
                map.replace(splitted[i], map.get(splitted[i]) + 1);
            }
            if (map.get(splitted[i]) > 1) {
                return splitted[i];
            }
        }
        return "";
    }

    public static boolean solution(String word1, String word2) {
        char[] chars1 = word1.toCharArray();
        char[] chars2 = word1.toCharArray();
        Arrays.sort(chars1);
        Arrays.sort(chars2);
        return Arrays.equals(chars1, chars2);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void getUserById_AuthorizedUser_ShouldReturnUser() {
        // Mock the behavior of UserRepository to return a User when findById is called with userId
        when(userRepository.findById(userId)).thenReturn(Optional.of(userWithAdminRole));
        when(authZService.CheckIfAuthorized(userId)).thenReturn(userWithAdminRole);
        // Mock the behavior of SecurityContextHolder to return a UserDetails object

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(userWithAdminRole.getUsername())
                .password(userWithAdminRole.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(userWithAdminRole.getRole().name())))
                .build();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "password");

        // Set the authentication context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertEquals(userWithAdminRole, result);
    }

    @Test
    public void getUserById_UnauthorizedUser_ShouldReturnUnauthorizedException() {
        when(authZService.CheckIfAuthorized(userId)).thenThrow(new UnauthorizedException("You are not authorized to access this resource"));
        // Assert
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> userService.getUserById(userId));
        Assertions.assertTrue(exception.getMessage().contains("You are not authorized to access this resource"));
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
