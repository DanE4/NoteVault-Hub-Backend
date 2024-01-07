package com.dane.notevault.service;

import com.dane.notevault.auth.Response;
import com.dane.notevault.dto.PostDTO;
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
