package com.grazac.springauthgrazac.user;


import com.grazac.springauthgrazac.user.dto.CreateUserRequest;
import com.grazac.springauthgrazac.user.dto.LoginRequest;
import com.grazac.springauthgrazac.user.dto.TokenPair;
import com.grazac.springauthgrazac.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public String updateToAdmin(){
        User loggedInUser = currentUserUtil.getLoggedInUser();
        String username = loggedInUser.getUsername();
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Optional<User> userExist =
                userRepository.findUserByUsername(username);
        if(userExist.isEmpty()) throw  new RuntimeException("user not found");
        if(userExist.get().getRole().name().equals("ROLE_ADMIN")) throw new RuntimeException("already an admin");

        userExist.get().setRole(Role.ROLE_ADMIN);
        userRepository.save(userExist.get());
        return "user updated";
    }

    public String createUser(CreateUserRequest request) {

        Boolean exist = userRepository.existsByEmail(request.getEmail());
        if(exist) throw new RuntimeException("USER ALREADY EXIST");

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
        return  "success";
    }

    public TokenPair loginUser(LoginRequest request) {
        // we this manager using custom approach
        // User user = userRepository.findByEmail(request.getEMail);
        // if(user.isEmpty) throw new RuntimeException("message");
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return jwtService.generateTokenPair(authenticate);
    }
}
