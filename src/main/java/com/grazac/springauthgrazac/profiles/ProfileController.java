package com.grazac.springauthgrazac.profiles;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/change-password")
    @PreAuthorize("hasRole('USER')")
    public String changePassword(@RequestBody ChangePasswordRequest request)  {
        return profileService.changePassword(request);
    }
}
