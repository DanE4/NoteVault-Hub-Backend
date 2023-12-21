package com.dane.homework_help.service;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.PostDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface PostService {
    PostDTO createPost(PostDTO post);

    PostDTO updatePost(UUID id, PostDTO post);

    PostDTO getPostById(UUID id);

    Response deletePostById(UUID id);

    List<PostDTO> getAllPosts();
}
