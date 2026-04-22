package com.grazac.springauthgrazac.utils;

import com.grazac.springauthgrazac.user.dto.TokenPair;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {

    // Step 1
    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private String jwtExpirationMs; // access token is usually short lived, 15 minutes - 1hour

    @Value("${app.jwt.refresh-expiration}")
    private String refreshExpirationMs; // 1 day -month or days


    //| Field             | Meaning                       |
//            | ----------------- | ----------------------------- |
//            | **principal**     | the user (UserDetails object) |
//            | **credentials**   | password or token             |
//            | **authorities**   | roles like ROLE_ADMIN         |
//            | **authenticated** | whether login succeeded       |
    // Setp 2:
    // Generic token util
    public String generateToken(Authentication authentication, long expirationMs, Map<String, String> claims) {

        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        log.info(String.valueOf(userPrincipal.getUsername()));
        Date now = new Date(); // current date
        Date expiryDate = new Date(now.getTime() + expirationMs); // currrent date + anytime of our choice

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(userPrincipal.getUsername())
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }


    public String generateCustomToken(String name, long expirationMs, Map<String, String> claims) {

        Date now = new Date(); // current date
        Date expiryDate = new Date(now.getTime() + expirationMs); // currrent date + anytime of our choice

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .subject(name)
                .claims(claims)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSignInKey())
                .compact();
    }


    // Step 3
    // access token and refreshtoken
    public String generateAccessToken(Authentication authentication) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put("tokenType", "access");     //
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        Set<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        //                                         36000000
        claims.put("role", String.valueOf(roles));


        return generateToken(authentication, Long.parseLong(jwtExpirationMs), claims);
    }

    public String generateRefreshToken(Authentication authentication) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put("tokenType", "refresh");
        //                                                    840000000000
        return generateToken(authentication, Long.parseLong(refreshExpirationMs), claims);
    }

    //
    public TokenPair generateTokenPair(Authentication authentication) {

        // Create access token
        String accessToken = generateAccessToken(authentication);

        // Create refresh token
        String refreshToken = generateRefreshToken(authentication);

        // Return both tokens wrapped in a DTO
        return new TokenPair(accessToken, refreshToken);
    }

    public boolean isRefreshToken(String token) {
        Claims claims = extractClaims(token);
        if (claims == null) {
            return false; // email,unique identifer
        }

        System.out.println("==+++++=========&&&&&&&&&&==========================");
        System.out.println(claims);
        System.out.println(claims.get("tokenType"));
        System.out.println("=====================================");
        return "refresh".equals(claims.get("tokenType")); // refresh != access
    }


    // 1 byte = 8 bits (0000 0000)
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Step 4 extract all claims i.e all payload like sub from the token
    private Claims extractClaims(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser().verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return claims;
    }

    // Step 5
    // extract username from token
    public String extractUsernameFromToken(String token) {
        Claims claims = extractClaims(token);
        if (claims != null) {
            return claims.getSubject(); // email or username or any unique id
        }
        return null;
    }

    // step 6 validate token for users
    public boolean isValidToken(String token) {
        return extractClaims(token) != null;
    }

    // Step 7 Vaidate token by checking username matches the user in spring
    public boolean validateTokenForUser(String token, UserDetails userDetails) {
        final String username = extractUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername()); // short circuit
    }


}
