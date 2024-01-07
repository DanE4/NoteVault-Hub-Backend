package com.dane.homework_help.api.repository;

import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ShouldSaveAll() {
        //Arrange
        User user = User.builder()
                .email("random@gmail.com")
                .password("$2a$12$sTJkWVSZRfAO/CzQC1fsaeXbNc1bQ21RAWTIwnWU70OrUyCXzii12")
                .username("random")
                .level(1)
                .points(0)
                .school("BGE")
                .role(Role.ADMIN)
                .build();
        //Act
        User savedUser = userRepository.save(user);
        //Assert
        //Assert
        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getLevel(), savedUser.getLevel());
        assertEquals(user.getPoints(), savedUser.getPoints());
        assertEquals(user.getSchool(), savedUser.getSchool());
        assertEquals(user.getRole(), savedUser.getRole());
    }

    @Test
    public void UserRepository_FindByEmail_ShouldReturnUser() {
        //Arrange
        //initialized from data.sql
        //Act
        User foundUser = userRepository.findByEmail("admn4testing1234+random@gmail.com");
        //Assert
        assertNotNull(foundUser);
    }

    //existsByUsername
    @Test
    public void UserRepository_ExistsByUsername_ShouldReturnTrue() {
        //Arrange
        //Act
        boolean exists = userRepository.existsByUsername("imanadmin");
        //Assert
        assertTrue(exists);
    }

    //findAll
    @Test
    public void UserRepository_FindAll_ShouldReturnAll() {
        //Arrange
        //initialized from data.sql
        //Act
        var users = userRepository.findAll();
        //Assert
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    @Test
    public void UserRepository_UserDelete_ShouldDelete() {
        var users = userRepository.findAll();
        userRepository.deleteById(users.get(0).getId());
        //Assert
        assertEquals(1, userRepository.findAll().size());
        assertFalse(userRepository.existsById(users.get(0).getId()));
    }

    @Test
    public void UserRepository_FindByEmail_ShouldReturnError() {
        //Arrange
        //initialized from data.sql
        //Act
        User foundUser = userRepository.findByEmail("asd");
        //Assert
        assertNull(foundUser);
    }
}
