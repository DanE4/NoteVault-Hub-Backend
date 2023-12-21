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
    @PostMapping("/post")
    public ResponseEntity<Response> createPost(@RequestBody PostDTO postData) {
        try {
            return ResponseEntity.ok()
                    .body(Response.builder().data(postService.createPost(postData)).build());
        } catch (UnauthorizedException e) {
            log.error("UnauthorizedException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        } catch (MissingJwtException e) {
            log.error("MissingJwtException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder().response("Missing JWT").build());
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
            log.error("UnauthorizedException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        } catch (RecordNotFoundException e) {
            log.error("RecordNotFoundException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().response("Post not found").build());
        }
    }

    //get all posts
    @Operation(
            description = "Endpoint for getting all posts",
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
                                                    [
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
                                                    ]
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
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> getAllPosts(HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(Response.builder().data(postService.getAllPosts()).build());
        } catch (UnauthorizedException e) {
            log.error("UnauthorizedException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        } catch (RecordNotFoundException e) {
            log.error("RecordNotFoundException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().response("Post not found").build());
        }
    }

    @Operation(
            description = "Endpoint for updating posts",
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
                                                        "id": "123e4567-e89b-12d3-a456-426655440001",
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
    @PutMapping("/{post_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> updatePost(@PathVariable(value = "post_id") UUID id,
                                               @RequestBody PostDTO postData, HttpServletRequest request) {
        try {
            return ResponseEntity.ok().body(Response.builder().data(postService.updatePost(id, postData)).build());
        } catch (UnauthorizedException e) {
            log.error("UnauthorizedException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        } catch (RecordNotFoundException e) {
            log.error("RecordNotFoundException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().response("Post not found").build());
        }
    }

    @Operation(
            description = "Endpoint for deleting posts",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @io.swagger.v3.oas.annotations.media.Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Response.class),
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "Response",
                                            value = """
                                                    {
                                                        "data": "Post deleted"
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

    @DeleteMapping("/{post_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<Response> deletePost(@PathVariable(value = "post_id") UUID id) {
        try {
            return ResponseEntity.ok().body(postService.deletePostById(id));
        } catch (UnauthorizedException e) {
            log.error("UnauthorizedException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Response.builder().response("Unauthorized").build());
        } catch (RecordNotFoundException e) {
            log.error("RecordNotFoundException: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Response.builder().response("Post not found").build());
        }
    }
}
