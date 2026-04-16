package com.grazac.springauthgrazac.profiles;

import com.grazac.springauthgrazac.config.CloudinaryService;
import com.grazac.springauthgrazac.exception.CustomBadRequestException;
import com.grazac.springauthgrazac.user.CurrentUserUtil;
import com.grazac.springauthgrazac.user.User;
import com.grazac.springauthgrazac.user.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ProfileService {
    private final CloudinaryService cloudinaryService;
    private final CurrentUserUtil currentUserUtil;
    private final PasswordEncoder passwordEncoder;
    private  final UserRepository userRepository;


    public ProfileService(CloudinaryService cloudinaryService, CurrentUserUtil currentUserUtil, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.cloudinaryService = cloudinaryService;
        this.currentUserUtil = currentUserUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public String upload(MultipartFile file) {
        // check if the file is not empty
        System.out.println("======================================");
        System.out.println(file);
        System.out.println("======================================");
        // image/png. imgae/jpeg, image/jpg or pdf
        if (file.isEmpty() || !Objects.requireNonNull(file.getContentType()).startsWith("image/"))
            throw new IllegalArgumentException("file is required and must be an image");

        // convert mb to byte
        if (file.getSize() > 1024 * 1024) {
            throw new IllegalArgumentException("file too large, max of 1mb");
        }

        try {
            return cloudinaryService.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String changePassword(ChangePasswordRequest request) {
        User loggedInUser = currentUserUtil.getLoggedInUser();

        boolean matches = passwordEncoder.matches(request.getOldPassword(), loggedInUser.getPassword());
        if (!matches) throw new CustomBadRequestException("password not match");
        // oldpass = string
        String newEncodedPass = passwordEncoder.encode(request.getNewPassword());
        String oldEncodePass = loggedInUser.getPassword();
        if (Objects.equals(newEncodedPass, oldEncodePass))
            throw new CustomBadRequestException("cannot use old password");

        loggedInUser.setPassword(newEncodedPass);
        userRepository.save(loggedInUser);

        return "success";
    }
}
