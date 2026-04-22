package com.grazac.springauthgrazac.user;


import com.grazac.springauthgrazac.audit.AuditLogService;
import com.grazac.springauthgrazac.audit.AuditRequest;
import com.grazac.springauthgrazac.exception.CustomBadRequestException;
import com.grazac.springauthgrazac.exception.ResourceNotFoundException;
import com.grazac.springauthgrazac.otp.*;
import com.grazac.springauthgrazac.savedtoken.Token;
import com.grazac.springauthgrazac.savedtoken.TokenService;
import com.grazac.springauthgrazac.user.dto.CreateUserRequest;
import com.grazac.springauthgrazac.user.dto.LoginRequest;
import com.grazac.springauthgrazac.user.dto.TokenPair;
import com.grazac.springauthgrazac.utils.EmailService;
import com.grazac.springauthgrazac.utils.JwtService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CurrentUserUtil currentUserUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuditLogService auditLogService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final OtpService otpService;
    private final OtpRepository otpRepository;
    private final EmailService emailService;
    private final UserDetailsService userDetailsService;
    private final TokenService tokenService;

    @Cacheable(value = "userCache", key = "#userId")
    // userCache:9876545678
    public User getUserById(Long userId) {
        log.info("======================fetching from db============================");
        log.info("======================fetching from db============================");
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("user with this id not found"));
    }

    @CacheEvict(value = "userCache", key = "#userId")
    public void deleteUserById(Long userId) {
        log.info("======================deleting from db============================");
        log.info("======================deleting from db============================");
//         userRepository.deleteById(userId);
    }

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
        OtpRequest otpRequest = OtpRequest.builder()
                .email(request.getEmail())
                .otpCode(passwordEncoder.encode(code)).purpose("verifyaccount").build();
        otpService.createOtp(otpRequest);

        userRepository.save(user);

        Map<String, Object> model = new HashMap<>();
        model.put("name", request.getName());
        model.put("otpCode", code);

        try {

            // FIRE AND FORGET ---> NON-BLOCKING
            emailService.sendEmail(
                    request.getEmail(),
                    "Verify your account",
                    "verification", // template name without `.html`
                    model
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "success";
    }

    // assumption : otp into their inbox
    public String verifyUser(OtpVerifyRequest request) {
        Optional<User> user = userRepository.findUserByEmail(request.getEmail());
        if (user.isEmpty()) throw new RuntimeException("user not found");
        if (user.get().getIsVerified() == true) throw new RuntimeException("account already verified");
        Optional<Otp> otp = otpService.findByEmailAndPurpose(request.getEmail(), "verifyaccount");
        if (otp.isEmpty()) throw new RuntimeException("otp not found");

        if (otp.get().getUsed() == true) throw new RuntimeException("otp already used");
        boolean isMatch = passwordEncoder.matches(request.getPlainOtp(), otp.get().getOtp());
        if (!isMatch) throw new RuntimeException("invalid otp");

        user.get().setIsVerified(true);
        otp.get().setUsed(true);
        otpRepository.save(otp.get());
        userRepository.save(user.get());

        return "success";


    }

    public TokenPair loginUser(LoginRequest request) {
        // we this manager using custom approach
        Optional<User> user = userRepository.findUserByUsername(request.getUsername());
        if (user.isEmpty()) throw new RuntimeException("User not found");

        if (user.get().getIsVerified() == false) throw new RuntimeException("User ont verified");
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        AuditRequest auditRequest = new AuditRequest();
        auditRequest.setAction("login");
        auditRequest.setUserId(user.get().getId());
        auditLogService.create(auditRequest);
        // cookies
        TokenPair tokenPair = jwtService.generateTokenPair(authenticate);
        tokenService.saveToken(tokenPair.getToken(), tokenPair.getRefreshToken());
        return tokenPair;
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

    @Transactional
    public void forgotPassword(ForgotPasswordRequest request) {
        // check if that email exist in db
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("email not found"));

        // get initial otp saved and delete it
        otpService.deleteOtp(user.getEmail(), "forgotPassword");

        // if email is found, save otp in db, with hashed code
        String code = UUID.randomUUID().toString().replace("-", "").substring(0, 4);
        System.out.println("=========================");
        System.out.println(code);
        System.out.println("=========================");
        OtpRequest otpRequest = OtpRequest.builder()
                .email(user.getEmail())
                .otpCode(passwordEncoder.encode(code)).purpose("forgotPassword").build();
        otpService.createOtp(otpRequest);


        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("email", user.getEmail());
        model.put("otpCode", code); // raw code to email

        try {

            // FIRE AND FORGET ---> NON-BLOCKING
            emailService.sendEmail(
                    user.getEmail(),
                    "Forgot password",
                    "forgotpassword", // template name without `.html`
                    model
            );
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        // send email containing the otp: otp or link
        // link: frontendlink?token=token
        // grazac.com?email="shayyo@gmail.com"&token=98765434567trertyuiuytrerty vs newPassword
        // otp vs newPassowrd and email

    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // check if that email exist in db
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("email not found"));

        // check if otp exists
        Optional<Otp> otp = otpService.findByEmailAndPurpose(request.getEmail(), "forgotPassword");
        if (otp.isEmpty()) throw new CustomBadRequestException("E0 - invalid otp");


        // match incoming otp
        System.out.println(otp.get().getOtp() + " " + otp.get().getEmail() + " " + otp.get().getPurpose());
        System.out.println(request.getOtpCode() + " " + otp.get().getOtp());
        boolean isMatch = passwordEncoder.matches(request.getOtpCode(), otp.get().getOtp());
        if (!passwordEncoder.matches(request.getOtpCode(), otp.get().getOtp()))
            throw new CustomBadRequestException("Invalid OTP");
        if (!isMatch) throw new CustomBadRequestException("invalid otp");

        // encode password
        String encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);

        // delete or set otp to use
        otpService.deleteOtp(request.getEmail(), "forgotPassword");
    }

    @Transactional
    public void resendPassword(ForgotPasswordRequest request) {
        this.forgotPassword(request);
    }

    public TokenPair refreshToken(RefreshTokenRequest request) {
        // check if refreshToken is revoked
        // if revoked, throw error out
        Optional<Token> foundToken
                = tokenService.findByRefreshToken(request.getRefreshToken());
        if(foundToken.get().isRefreshTokenRevoked()
        ){
            throw  new CustomBadRequestException("Revoked:: invalid token");
        }

        // to avoid bloating that db, use cron job to delete token that the date created + 10 days

        String refreshToken = request.getRefreshToken();
        if(!jwtService.isRefreshToken(refreshToken)){
            throw  new CustomBadRequestException("E0 - invalid token");
        }

        // upload login: access token and refresh token --> generates a new access token (15 minutes)
        String user = jwtService.extractUsernameFromToken(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        if(userDetails == null){
            throw  new CustomBadRequestException("user does not exist");
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails,null, userDetails.getAuthorities()
        );

        // revoke/invalidate old token ---> use redis, save it against timestamp (tuesday)
        // for every token you generate ---> have a flag of revoked (default) == false
        // revoke both access and refreshtoken

        //invalidate old token
        tokenService.revoke(refreshToken);

        // cookies
        String accessToken = jwtService.generateAccessToken(authentication);
//        String newRefreshToken = jwtService.generateRefreshToken(authentication);

        return new TokenPair(accessToken, refreshToken);

    }
}
