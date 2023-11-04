package com.dane.homework_help.service.impl;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.entity.Post;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.mapper.PostMapper;
import com.dane.homework_help.repository.PostRepository;
import com.dane.homework_help.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/*
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<PostToSubject> subjects;

    @OneToMany(mappedBy = "post")
    private List<File> files;
}

 */
@Service
@Slf4j
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private PostMapper postMapper;

    @Override
    public PostDTO createPost(PostDTO postData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        Post post = Post.builder()
                .title(postData.title())
                .content(postData.content())
                .user(User.builder().id(((User) userDetails).getId()).build())
                .build();


        return postMapper.apply(postRepository.save(post));
    }


    @Override
    public Post updatePost(Post post) {
        return null;
    }

    @Override
    public Post getPostById(String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return null;
    }

    @Override
    public Response deletePostById(String id) {
        return null;
    }
}
