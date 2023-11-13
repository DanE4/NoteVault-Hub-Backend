package com.dane.homework_help;

import com.dane.homework_help.entity.Post;
import com.dane.homework_help.entity.Subject;
import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.repository.PostRepository;
import com.dane.homework_help.repository.SubjectRepository;
import com.dane.homework_help.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Init {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SubjectRepository subjectRepository;

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

        var post = Post.builder()
                .title("Calculus 1")
                .content("Mathematics")
                .user(user)
                .build();

        userRepository.save(user);
        postRepository.save(post);
        subjectRepository.save(subject);
        subjectRepository.save(subject2);

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
