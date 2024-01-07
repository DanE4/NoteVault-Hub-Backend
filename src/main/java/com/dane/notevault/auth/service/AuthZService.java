package com.dane.notevault.auth.service;

import com.dane.notevault.entity.User;
import com.dane.notevault.exception.UnauthorizedException;
import com.dane.notevault.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthZService {
    private final UserRepository userRepository;

    public AuthZService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User CheckIfAuthorized(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User extractedUser = userRepository.findByEmail(email);

        if (extractedUser == null) {
            throw new UsernameNotFoundException("User with email " + email + " not found");
        }
        if (!extractedUser.getId().equals(id) && extractedUser.getAuthorities()
                .stream()
                .noneMatch(a -> a.toString().equals("ADMIN"))) {
            throw new UnauthorizedException("You are not authorized to access this resource");
        }
        return extractedUser;
    }

    public User ExtractUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return userRepository.findByEmail(email);
    }
}
