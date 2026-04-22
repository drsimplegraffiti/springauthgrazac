package com.grazac.springauthgrazac.user;

import com.grazac.springauthgrazac.otp.OtpVerifyRequest;
import com.grazac.springauthgrazac.user.dto.CreateUserRequest;
import com.grazac.springauthgrazac.user.dto.LoginRequest;
import com.grazac.springauthgrazac.user.dto.TokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth") // api/v1/auth/register, api/v1/auth/login will be permitted ignored
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    // authentication + authorization
    @PutMapping("/switch/role")
    @PreAuthorize("hasRole('USER')")
//    @PreAuthorize("hasAllRoles('ADMIN')")
    public String updateUser() {
        return authService.updateToAdmin();
    }

    @PostMapping("/create")
    public String createUser(@RequestBody CreateUserRequest request) {
        return authService.createUser(request);
    }

    // access token shortlived
    @PostMapping("/login")
    public TokenPair loginUser(@RequestBody LoginRequest request) {
        return authService.loginUser(request);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
        var response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }

    //    verifyUser
    @PostMapping("/verify")
    public String verifyUser(@RequestBody OtpVerifyRequest request) {
        return authService.verifyUser(request);
    }

    @PostMapping("/custom")
    public String customToken(@RequestBody String name) {
        return authService.customTokenGen(name);
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Long id) {
        return authService.getUserById(id);
    }

    @DeleteMapping("/user/{id}")
    public void deleteById(@PathVariable Long id) {
        authService.deleteUserById(id);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("otp sent successfully");

    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok("password reset successfully");
    }

    @PostMapping("/resend-password")
    public ResponseEntity<?> resendPassword(@RequestBody ForgotPasswordRequest request) {
        authService.resendPassword(request);
        return ResponseEntity.ok("password reset successfully");
    }
}
