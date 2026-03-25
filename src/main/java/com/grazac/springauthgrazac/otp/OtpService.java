package com.grazac.springauthgrazac.otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
public class OtpService {

    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;

    public OtpService(OtpRepository otpRepository, PasswordEncoder passwordEncoder) {
        this.otpRepository = otpRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createOtp(OtpRequest request){
        Otp otp = Otp.builder()
                .email(request.getEmail()).
                otp(passwordEncoder.encode(request.getOtpCode()))
                .purpose(request.getPurpose()).used(false).build();
        otpRepository.save(otp);
    }

    public Optional<Otp> findByEmailAndPurpose(String email, String verifyaccount) {
        return otpRepository.findOtpByEmailAndPurposeContainingIgnoreCase(email, verifyaccount);
    }
}
