package com.grazac.springauthgrazac.user;


import com.grazac.springauthgrazac.otp.*;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;

    public String updateToAdmin() {
        User loggedInUser = currentUserUtil.getLoggedInUser();
        String username = loggedInUser.getUsername();
        System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        Optional<User> userExist =
                userRepository.findUserByUsername(username);
        if (userExist.isEmpty()) throw new RuntimeException("user not found");
        if (userExist.get().getRole().name().equals("ROLE_ADMIN")) throw new RuntimeException("already an admin");

        userExist.get().setRole(Role.ROLE_ADMIN);
        userRepository.save(userExist.get());
        return "user updated";
    }

    public String createUser(CreateUserRequest request) {

        Boolean exist = userRepository.existsByEmail(request.getEmail());
        if (exist) throw new RuntimeException("USER ALREADY EXIST");

        User user = User.builder()
                .email(request.getEmail())
                .isVerified(false)
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        System.out.println("micmick email sender: " + code);
        OtpRequest otpRequest = OtpRequest.builder().email(request.getEmail()).otpCode(code).purpose("verifyaccount").build();
        otpService.createOtp(otpRequest);

        userRepository.save(user);
        return "success";
    }

    // assumption : otp into their inbox
    public String verifyUser(OtpVerifyRequest request){
        Optional<User> user = userRepository.findUserByEmail(request.getEmail());
        if(user.isEmpty()) throw new RuntimeException("user not found");
        if(user.get().getIsVerified() == true) throw new RuntimeException("account already verified");
        Optional<Otp> otp = otpService.findByEmailAndPurpose(request.getEmail(), "verifyaccount");
        if(otp.isEmpty()) throw new RuntimeException("otp not found");

        if(otp.get().getUsed() == true) throw new RuntimeException("otp already used");
        boolean isMatch = passwordEncoder.matches(request.getPlainOtp(), otp.get().getOtp());
        if(!isMatch) throw new RuntimeException("invalid otp");

        user.get().setIsVerified(true);
        otp.get().setUsed(true);
        otpRepository.save(otp.get());
        userRepository.save(user.get());

        return "success";


    }

    public TokenPair loginUser(LoginRequest request) {
        // we this manager using custom approach
         Optional<User> user = userRepository.findUserByUsername(request.getUsername());
         if(user.isEmpty()) throw new RuntimeException("User not found");

         if(user.get().getIsVerified() == false)  throw new RuntimeException("User ont verified");
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return jwtService.generateTokenPair(authenticate);
    }

    public String customTokenGen(String name) {

        String expirationMs = "3600000";
        Date now = new Date(); // current date
//        Date expiryDate = new Date(now.getTime() + expirationMs);
        Map<String, String> claims = new HashMap<>();
        claims.put("sub", name);
        claims.put("role", "admin");
        return jwtService.generateCustomToken(name, Long.parseLong(expirationMs), claims);
    }
}
