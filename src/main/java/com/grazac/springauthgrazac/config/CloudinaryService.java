package com.grazac.springauthgrazac.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service // @Compoent
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // https://res.cloudinary.com/doyuyy4g6/image/upload/f_auto,q_auto/Black_Modern_Vlogger_YouTube_Banner_1_uygh8w
    public String uploadFile(MultipartFile file) throws IOException {
        var uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("folder", "grazac"));
// return (String) uploadResult.get("secure_url");
        System.out.println(uploadResult);
        return uploadResult.get("secure_url").toString();

    }

}
