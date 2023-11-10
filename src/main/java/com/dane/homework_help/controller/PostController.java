package com.dane.homework_help.controller;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.exception.MissingJwtException;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public ResponseEntity<Response> createPost(HttpServletRequest request, @RequestBody PostDTO postData) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder().data(postService.createPost(postData)).build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (MissingJwtException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
