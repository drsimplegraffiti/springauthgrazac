package com.grazac.springauthgrazac.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair {
    private String token;
    private String refreshToken;
}
