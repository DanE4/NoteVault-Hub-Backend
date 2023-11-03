package com.dane.homework_help.mapper;

import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDTO mapToDto(Post post);

    List<PostDTO> mapToDtos(List<Post> posts);

    @Mapping(target = "postDto.id", source = "id")
    @Mapping(target = "postDto.title", source = "title")
    @Mapping(target = "postDto.content", source = "content")
    @Mapping(target = "postDto.user", source = "user")
    @Mapping(target = "postDto.subjects", source = "subjects")
    @Mapping(target = "postDto.files", source = "files")
    Post signUpToPost(PostDTO postDto);
}

/*
@Service
public class PostDTOMapper implements Function<Post, PostDTO> {
    private final UserDTOMapper userDTOMapper;

    public PostDTOMapper(UserDTOMapper userDTOMapper) {
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
*/