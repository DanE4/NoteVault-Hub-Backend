package com.dane.homework_help.api.repository;

import com.dane.homework_help.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PostRepositoryTests {
    private final PostRepository postRepository;

    public PostRepositoryTests(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Test
    public void PostRepository_SaveAll_ShouldSaveAll() {
        //Arrange
        //Act
        //Assert
    }


}
