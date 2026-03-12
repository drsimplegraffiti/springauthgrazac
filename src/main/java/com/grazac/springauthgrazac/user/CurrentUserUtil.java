package com.grazac.springauthgrazac.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component // Marks this class as a Spring Bean so Spring can automatically detect and manage it
public class CurrentUserUtil {

    private final UserRepository userRepository;

    public CurrentUserUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getLoggedInUsername() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public User getLoggedInUser() {
        String username = getLoggedInUsername();
        return userRepository.findUserByEmail(username)

                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
