package com.dane.homework_help.service;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.entity.Post;
import org.springframework.stereotype.Service;

@Service
public interface PostService {
    PostDTO createPost(PostDTO post);

    Post updatePost(Post post);

    Post getPostById(String id);

    Response deletePostById(String id);
}
