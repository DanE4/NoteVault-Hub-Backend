package com.dane.notevault;

import com.dane.notevault.entity.Post;
import com.dane.notevault.entity.PostToSubject;
import com.dane.notevault.entity.Subject;
import com.dane.notevault.entity.User;
import com.dane.notevault.entity.enums.Role;
import com.dane.notevault.repository.PostRepository;
import com.dane.notevault.repository.PostToSubjectRepository;
import com.dane.notevault.repository.SubjectRepository;
import com.dane.notevault.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class Init {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SubjectRepository subjectRepository;
    private final PostToSubjectRepository postToSubjectRepository;

    @PostConstruct
    @Transactional
    public void init() {
        var user = User.builder()
                .username("user")
                .email("asdasdasd+asd@gmail.com")
                .level(1)
                .points(100)
                .school("BGE")
                .password("$2a$12$os/7cWt2mtb0n3FdahZyOePi1w.xICQ/uXDkLrL/x3bALQg/dS5oe")
                .role(Role.USER)
                .build();
        var subject = Subject.builder()
                .name("Calculus 1")
                .description("Mathematics")
                .build();
        var subject2 = Subject.builder()
                .name("Calculus 2")
                .description("Mathematics")
                .build();

        Post post = Post.builder()
                .title("Calculus 1")
                .content("Mathematics")
                .user(user)
                .subjects(new ArrayList<>())
                .build();

        var posttoSubject = PostToSubject.builder()
                .post(post)
                .subject(subject)
                .build();
        var posttoSubject2 = PostToSubject.builder()
                .post(post)
                .subject(subject2)
                .build();
        post.getSubjects().add(posttoSubject);
        post.getSubjects().add(posttoSubject2);


        userRepository.save(user);
        postRepository.save(post);
        subjectRepository.save(subject);
        subjectRepository.save(subject2);
        postToSubjectRepository.save(posttoSubject);
        postToSubjectRepository.save(posttoSubject2);
    }
    /*
    INSERT INTO users (id, email, password, role, username, level, points, school, created_at, updated_at)
VALUES ('123e4567-e89b-12d3-a456-426655440000', 'admn4testing1234+asd@gmail.com', '$2a$12$os/7cWt2mtb0n3FdahZyOePi1w
.xICQ/uXDkLrL/x3bALQg/dS5oe', 'ADMIN',
        'imanadmin', 20, 20000, 'BME', now(), now());
INSERT INTO users (id, email, password, role, username, level, points, school, created_at, updated_at)
VALUES ('123e4567-e89b-12d3-a456-426655440055', 'admn4testing1234+random@gmail.com',
        '$2a$12$os/7cWt2mtb0n3FdahZyOePi1w.xICQ/uXDkLrL/x3bALQg/dS5oe', 'ADMIN',
        'imanadmin2', 10, 5000, 'ELTE', now(), now());
     */
}
