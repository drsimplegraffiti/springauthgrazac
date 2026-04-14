package com.grazac.springauthgrazac.profiles;

import com.grazac.springauthgrazac.config.CloudinaryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
public class ProfileService {
    private final CloudinaryService cloudinaryService;

    public ProfileService(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    public String upload(MultipartFile file)  {
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
}
