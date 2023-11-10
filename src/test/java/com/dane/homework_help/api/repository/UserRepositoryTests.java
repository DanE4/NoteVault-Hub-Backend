package com.dane.homework_help.api.repository;

import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
                .role(Role.ADMIN)
                .build();
        //Act
        User savedUser = userRepository.save(user);
        //Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    public void UserRepository_FindByEmail_ShouldReturnUser() {
        //Arrange
        //initialized from data.sql
        //Act
        User foundUser = userRepository.findByEmail("admn4testing1234+random@gmail.com");
        //Assert
        Assertions.assertThat(foundUser).isNotNull();
    }

    //existsByUsername
    @Test
    public void UserRepository_ExistsByUsername_ShouldReturnTrue() {
        //Arrange
        //initialized from data.sql
        //Act
        boolean exists = userRepository.existsByUsername("imanadmin");
        //Assert
        Assertions.assertThat(exists).isTrue();
    }

    //findAll
    @Test
    public void UserRepository_FindAll_ShouldReturnAll() {
        //Arrange
        //initialized from data.sql
        //Act
        var users = userRepository.findAll();
        //Assert
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(users.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_UserDelete_ShouldDelete() {

        userRepository.deleteById(1);
        //Assert
        Assertions.assertThat(userRepository.existsById(1)).isFalse();
    }
}
