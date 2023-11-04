package com.dane.homework_help.mapper;

import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.entity.Post;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostMapper implements Function<Post, PostDTO> {
    private final UserMapper userDTOMapper;

    public PostMapper(UserMapper userDTOMapper) {
        this.userDTOMapper = userDTOMapper;
    }

    @Override
    public PostDTO apply(Post post) {
        return new PostDTO(post.getId(), post.getTitle(), post.getContent(), userDTOMapper.apply(post.getUser()), post.getSubjects(), post.getFiles());
    }

    public Post mapToEntity(PostDTO postDTO) {
        return Post.builder()
                .id(postDTO.id())
                .title(postDTO.title())
                .content(postDTO.content())
                .user(userDTOMapper.mapToEntity(postDTO.user()))
                .subjects(postDTO.subjects())
                .files(postDTO.files())
                .build();
    }

}
