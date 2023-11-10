package com.dane.homework_help.mapper;

import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.entity.Post;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostMapper implements Function<Post, PostDTO> {
    public final UserMapper userDTOMapper;
    public final FileMapper fileMapper;

    public PostMapper(UserMapper userDTOMapper, FileMapper fileMapper) {
        this.userDTOMapper = userDTOMapper;
        this.fileMapper = fileMapper;
    }

    @Override
    public PostDTO apply(Post post) {
        return new PostDTO(post.getId(), post.getTitle(), post.getContent(), post.getUserId(),
                //map all files to DTOs
                post.getSubjectsIds(), post.getFilesIds());
    }
}
