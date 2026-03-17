package com.grazac.springauthgrazac.user.dto;


import com.grazac.springauthgrazac.user.User;
import com.grazac.springauthgrazac.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class CustomDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("========================");
        System.out.println(username);
        System.out.println("=========================");
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info(user.getEmail());
        log.info(user.getUsername());
        org.springframework.security.core.userdetails.User user1 = new org.springframework.security.core.userdetails.User(

                user.getUsername(), // ronaldo
                user.getPassword(),
                getAuthority(user)
        );
        log.info(String.valueOf(user1));
        return user1;
    }

    // Method to convert the user's role into a Spring Security authority
    private Collection<? extends GrantedAuthority> getAuthority(User user) {

        // Create an authority using the role name from the User entity
        // Example: ROLE_ADMIN or ROLE_USER
        GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());

        // Return the authority as a list (Spring expects a collection)
        return List.of(authority);
    }
}