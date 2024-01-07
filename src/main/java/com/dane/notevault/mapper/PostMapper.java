package com.dane.notevault.mapper;

import com.dane.notevault.dto.PostDTO;
import com.dane.notevault.entity.Post;
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
                .subjectIds(post.getSubjectIds())
                .fileIds(post.getFilesIds())
                .build();
    }
}
