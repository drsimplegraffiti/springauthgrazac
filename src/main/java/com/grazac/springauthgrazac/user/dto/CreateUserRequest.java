package com.grazac.springauthgrazac.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserRequest {
    private String email;
    private String name;
    private String username;
    private String password;
}
