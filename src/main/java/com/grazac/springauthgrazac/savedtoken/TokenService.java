package com.grazac.springauthgrazac.savedtoken;


import com.grazac.springauthgrazac.exception.CustomBadRequestException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepo;

    public TokenService(TokenRepository tokenRepo) {
        this.tokenRepo = tokenRepo;
    }

    public void saveToken(String accessToken, String refreshToken) {
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isRefreshTokenRevoked(false)
                .isAccessTokenRevoked(false).build();
        tokenRepo.save(token);
    }


    public void revoke(String refreshToken) {
        Optional<Token> token =
                tokenRepo.findByRefreshToken(refreshToken);
        if (token.isEmpty()) throw new CustomBadRequestException("invalid token");
        token.get().setAccessTokenRevoked(true);
        token.get().setRefreshTokenRevoked(true);
        tokenRepo.save(token.get());


    }

    public Optional<Token> findByAccessToken(String accessToken) {
        Optional<Token> token =
                tokenRepo.findByAccessToken(accessToken);
        if(token.isEmpty()) throw new CustomBadRequestException("not found");
        return token;
    }

    public Optional<Token> findByRefreshToken(String refreshToken) {
        Optional<Token> token =
                tokenRepo.findByRefreshToken(refreshToken);
        if(token.isEmpty()) throw new CustomBadRequestException("not found");
        return token;
    }
}
