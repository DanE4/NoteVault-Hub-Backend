package com.dane.homework_help.api.repository;

import com.dane.homework_help.entity.Post;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepository;

    @Test
    public void PostRepository_SaveAll_ShouldSaveAll() {
        //Arrange
        Post post = Post.builder()
                .title("random")
                .content("random")
                .user(new User())
                .build();
        //Act
        Post savedPost = postRepository.save(post);
        //Assert
        assertNotNull(savedPost);
    }

    @Test
    public void PostRepository_FindById_ShouldReturnPost() {
        //Arrange
        //initialized from data.sql
        //Act
        Post foundPost = postRepository.findById(UUID.fromString("123e4567-e89b-12d3-a456-426655440001")).orElse(null);
        //Assert
        assertNotNull(foundPost);
    }

    @Test
    public void PostRepository_FindAll_ShouldReturnAll() {
        //Arrange
        //initialized from data.sql
        //Act
        var foundPosts = postRepository.findAll();
        //Assert
        assertNotNull(foundPosts);
    }

    @Test
    public void PostRepository_DeleteById_ShouldDeletePost() {
        //Arrange
        //initialized from data.sql
        //Act
        postRepository.deleteById(UUID.fromString("123e4567-e89b-12d3-a456-426655440001"));
        //Assert
        assertFalse(postRepository.existsById(UUID.fromString("123e4567-e89b-12d3-a456-426655440001")));
    }

    @Test
    public void PostRepository_FindId_ShouldReturnNull() {
        //Arrange
        //initialized from data.sql
        //Act
        Post foundPost = postRepository.findById(UUID.fromString("123e4567-e89b-12d3-a456-426655440002")).orElse(null);
        //Assert
        assertNull(foundPost);
    }
}
