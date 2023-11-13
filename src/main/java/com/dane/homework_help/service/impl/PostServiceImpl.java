package com.dane.homework_help.service.impl;

import com.dane.homework_help.auth.Response;
import com.dane.homework_help.auth.service.AuthZService;
import com.dane.homework_help.dto.PostDTO;
import com.dane.homework_help.entity.Post;
import com.dane.homework_help.entity.PostToSubject;
import com.dane.homework_help.entity.Subject;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.exception.RecordNotFoundException;
import com.dane.homework_help.exception.UnauthorizedException;
import com.dane.homework_help.mapper.PostMapper;
import com.dane.homework_help.repository.PostRepository;
import com.dane.homework_help.repository.PostToSubjectRepository;
import com.dane.homework_help.repository.SubjectRepository;
import com.dane.homework_help.repository.UserRepository;
import com.dane.homework_help.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

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
@Slf4j
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostMapper postMapper;
    private final PostRepository postRepository;
    private final AuthZService authZService;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final PostToSubjectRepository postToSubjectRepository;

    @Override
    public PostDTO createPost(PostDTO postData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User extractedUser = userRepository.findByEmail(email);

        Post post = Post.builder()
                .title(postData.title())
                .content(postData.content())
                .user(extractedUser)
                .build();
        postRepository.save(post);

        Optional.ofNullable(postData.fileIds()).ifPresent(post::setFilesIds);
        Optional.ofNullable(postData.subjectIds()).ifPresent(subjectIds -> {
            post.setSubjectIds(subjectIds);
            postToSubjectRepository.deleteAllByPostId(post.getId());
            subjectIds.forEach(subjectId -> {
                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new RecordNotFoundException("Subject with id " + subjectId +
                                " not found"));
                PostToSubject postToSubject = PostToSubject.builder()
                        .post(post)
                        .subject(subject)
                        .build();
                postToSubjectRepository.save(postToSubject);
            });
        });
        log.info("Post created");
        return postMapper.apply(post);
    }


    @Override
    @CachePut(value = "post", key = "#id")
    public PostDTO updatePost(UUID id, PostDTO postData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        //update post in postrepository

        User extractedUser = userRepository.findByEmail(email);

        Post post = postRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Post with id " + id +
                " not found"));
        if (post.getUserId() != extractedUser.getId()) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        //only update those what are not null in PostDTO
        Optional.ofNullable(postData.title()).ifPresent(post::setTitle);
        Optional.ofNullable(postData.content()).ifPresent(post::setContent);

        Optional.ofNullable(postData.subjectIds()).ifPresent(subjectIds -> {
            postToSubjectRepository.deleteAllByPostId(post.getId());
            subjectIds.forEach(subjectId -> {
                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new RecordNotFoundException("Subject with id " + subjectId +
                                " not found"));
                PostToSubject postToSubject = PostToSubject.builder()
                        .post(post)
                        .subject(subject)
                        .build();
                postToSubjectRepository.save(postToSubject);
            });
        });

        return postMapper.apply(postRepository.save(post));
    }

    @Override
    @Cacheable(value = "post", key = "#id")
    public PostDTO getPostById(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User extractedUser = userRepository.findByEmail(email);

        Post post = postRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Post with id " + id +
                " not found"));
        if (post.getUserId() != extractedUser.getId()) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        return postMapper.apply(post);
    }

    @Override
    public Response deletePostById(String id) {
        return null;
    }
}
