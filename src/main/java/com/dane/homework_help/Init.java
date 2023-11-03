package com.dane.homework_help;

import com.dane.homework_help.entity.User;
import com.dane.homework_help.entity.enums.Role;
import com.dane.homework_help.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Init {
    private final UserRepository userRepository;

    @PostConstruct
    @Transactional
    public void init() {
        var user = User.builder()
                .username("user")
                .email("asdasdasd+asd@gmail.com")
                .password("$2a$12$os/7cWt2mtb0n3FdahZyOePi1w.xICQ/uXDkLrL/x3bALQg/dS5oe")
                .role(Role.ADMIN)
                .build();
        userRepository.save(user);
    }
}
