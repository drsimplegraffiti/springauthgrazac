package com.grazac.springauthgrazac.savedtoken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessTokenAndRefreshToken(String accessToken, String refreshToken);


    @Query(value = "select * from token where refresh_token=:refreshToken", nativeQuery = true)
    Optional<Token> findByRefreshToken(String refreshToken);

    @Query(value = "select * from token where access_token=:accessToken", nativeQuery = true)
    Optional<Token> findByAccessToken(String accessToken);
}

