package com.dane.homework_help.repository;

import com.dane.homework_help.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {

}
