package com.dane.homework_help.mapper;

import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.entity.Post;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class PostMapper implements Function<Post, PostDTO> {
    @Override
    public PostDTO apply(Post post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUserId())
                .subjectIds(post.getSubjectsIds())
                .fileIds(post.getFilesIds())
                .build();
    }
}
