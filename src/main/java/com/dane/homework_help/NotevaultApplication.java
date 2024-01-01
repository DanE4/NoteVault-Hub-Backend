package com.dane.homework_help;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class NotevaultApplication {
    public static void main(String[] args) {
        //kill anything on 8080 port
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command("bash", "-c", "fuser -k 8080/tcp");
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SpringApplication.run(NotevaultApplication.class, args);
    }
/*
    @Bean
    public CommandLineRunner commandLineRunner(
            AuthenticationService service
    ) {
        return args -> {
            var admin = RegisterRequest.builder()
                    .username("admin1")
                    .email("asdasdasd+asd@gmail.com")
                    .password("$2a$12$os/7cWt2mtb0n3FdahZyOePi1w.xICQ/uXDkLrL/x3bALQg/dS5oe")
                    .role(Role.ADMIN)
                    .build();
            System.out.println("Admin tokens: " + service.register(admin).getData());

            var secondAdmin = RegisterRequest.builder()
                    .username("admin2")
                    .email("admn4testing1234+random@gmail.com")
                    .password("$2a$12$os/7cWt2mtb0n3FdahZyOePi1w.xICQ/uXDkLrL/x3bALQg/dS5oe")
                    .role(Role.ADMIN)
                    .build();
            System.out.println("Admin tokens: " + service.register(secondAdmin).getData());
        };
    }
 */
}
