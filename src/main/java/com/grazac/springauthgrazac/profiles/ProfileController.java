package com.grazac.springauthgrazac.profiles;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profiles") //
public class ProfileController {


    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }
    @PostMapping("/create")
    public String uploadFile(@RequestParam MultipartFile file)  {
        return profileService.upload(file);
    }
}
