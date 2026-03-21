package com.grazac.springauthgrazac.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // automatic bean
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("REQUEST: {} {}", request.getMethod(), request.getRequestURI());  // uri and url
        filterChain.doFilter(request, response); // next function
        log.info("RESPONSE: {} {}", response.getStatus(), response.getHeader("Authorization"));
    }
}
