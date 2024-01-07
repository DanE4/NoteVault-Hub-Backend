package com.dane.notevault.service.impl;

import com.dane.notevault.auth.Response;
import com.dane.notevault.auth.service.AuthZService;
import com.dane.notevault.dto.PostDTO;
import com.dane.notevault.entity.Post;
import com.dane.notevault.entity.PostToSubject;
import com.dane.notevault.entity.Subject;
import com.dane.notevault.entity.User;
import com.dane.notevault.exception.RecordNotFoundException;
import com.dane.notevault.mapper.PostMapper;
import com.dane.notevault.repository.PostRepository;
import com.dane.notevault.repository.PostToSubjectRepository;
import com.dane.notevault.repository.SubjectRepository;
import com.dane.notevault.repository.UserRepository;
import com.dane.notevault.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
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
        authZService.CheckIfAuthorized(id);
        Post post = postRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Post with id " + id +
                " not found"));
        //Only updates what are not null in PostDTO
        Optional.ofNullable(postData.title()).ifPresent(post::setTitle);
        Optional.ofNullable(postData.content()).ifPresent(post::setContent);
        Optional.ofNullable(postData.subjectIds()).ifPresent(subjectIds -> {
            postToSubjectRepository.deleteAllByPostId(id);
            subjectIds.forEach(subjectId -> {
                Subject subject = subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new RecordNotFoundException("Subject with id " + subjectId +
                                " not found"));
                PostToSubject postToSubject = PostToSubject.builder()
                        .post(post)
                        .subject(subject)
                        .build();
                postToSubjectRepository.save(postToSubject);
                post.getSubjects().add(postToSubject);
            });
        });
        return postMapper.apply(postRepository.save(post));
    }

    @Override
    @Cacheable(value = "post", key = "#id")
    public PostDTO getPostById(UUID id) {
        /*
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User extractedUser = userRepository.findByEmail(email);

        Post post = postRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Post with id " + id +
                " not found"));
        if (post.getUserId() != extractedUser.getId()) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        */
        authZService.CheckIfAuthorized(id);
        Post post = postRepository.findById(id).orElseThrow(()-> new RecordNotFoundException("Post with "+id+" not " + "found"));
        return postMapper.apply(post);
    }

    @Override
    public List<PostDTO> getAllPosts(){
        return postRepository.findAll().stream().map(postMapper).toList();
    }

    @Override
    public Response deletePostById(UUID id) {
        authZService.CheckIfAuthorized(id);
        postToSubjectRepository.deleteAllByPostId(id);
        postRepository.deleteById(id);
        return Response.builder().data("Post deleted").build();
    }
}
