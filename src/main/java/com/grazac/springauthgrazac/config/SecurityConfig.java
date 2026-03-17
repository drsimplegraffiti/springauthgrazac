package com.grazac.springauthgrazac.config;


import com.grazac.springauthgrazac.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor // constructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService; // spring
    private final JwtAuthenticationFilter jwtAuthenticationFilter; // custom jwt handler
    // step 1
    // whitelisted url --> open route such login, signup, swagger, products
private static  final String[] WHITE_LIST_URL = {
        "/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html",
            "/error"
    };

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

// Step 2
    // DaoProvide
    // it connects spring security login system to your database + password hashing

    @Bean
    public AuthenticationProvider authenticationProvider() {

        // DaoAuthenticationProvider loads user using UserDetailsService
        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService);

        // Set password encoder for verifying passwords
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    // step 3
@Bean
    public  AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig){
        return authConfig.getAuthenticationManager();
}

// step 4
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .cors(c -> c.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(
                    req -> req
                            // if(WHILE_LIST_UTL.CONTAINS("AUTH/**) || CONTAINS("ERROR) permit
                            .requestMatchers(WHITE_LIST_URL).permitAll()
                            .anyRequest().authenticated()
            )
            .sessionManagement(
                    session -> session
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            )
            .authenticationProvider(authenticationProvider());
    return http.build();
}

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(
                "http://localhost:63342",
                "http://localhost:3000",
                "https://secsystem-emr.vercel.app",
                "https://emr-sigma-ten.vercel.app",
                "Postman"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true); // Allow cookies or Authorization headers

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }





}
