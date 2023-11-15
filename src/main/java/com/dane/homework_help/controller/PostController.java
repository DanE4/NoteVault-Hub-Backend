package com.dane.homework_help.controller;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.exception.MissingJwtException;
import com.dane.homework_help.exception.RecordNotFoundException;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@Tag(name = "Post")
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }


    @Operation(
            description = "Endpoint for creating posts",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
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

    @Operation(
            description = "Endpoint for getting posts",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostDTO.class),
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Post",
                                            value = """
                                                    {
                                                        "id": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                        "title": "This is my new post",
                                                        "content": "lorem ipsum",
                                                        "userId": "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11",
                                                        "subjectIds": [
                                                            "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
                                                        ],
                                                        "fileIds": [
                                                            "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11"
                                                        ]
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            description = "Unauthorized / Invalid Token",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "Not Found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/{post_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> getPostById(@PathVariable(value = "post_id") UUID id,
                                                HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(Response.builder().data(postService.getPostById(id)).build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{post_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> updatePost(@PathVariable(value = "post_id") UUID id,
                                               @RequestBody PostDTO postData, HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(Response.builder().data(postService.updatePost(id, postData)).build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (RecordNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
