package com.dane.homework_help.repository;

import com.dane.homework_help.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
    //sql injection safe query
    @Query("SELECT u FROM User u WHERE u.username = :username")
    User findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(String email);

    boolean existsByUsername(String username);

    List<User> findAll();

    Optional<User> findById(UUID id);

    String sqlQuery = "SELECT * FROM products WHERE name = '" + "name" + "'";
}
