package com.grazac.springauthgrazac.filters;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter implements Filter {
    private final RedisTemplate redisTemplate;
    private static final int LIMIT = 3;
    private static final long WINDOW_DURATION = 60;

    public RateLimitFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String clientIp = httpRequest.getRemoteAddr();
        System.out.println(clientIp);
        String key = "rate_limit:" + clientIp;
        System.out.println(key);

//        Long requests = redisTemplate.opsForValue().increment(key, 1);
//        redisTemplate.opsForValue().set("aim", "done");
        redisTemplate.opsForValue().set("aim", 100, 30, TimeUnit.MINUTES); // Set with expiration
//        if (requests == 1) {
//            // redis.expire
//            redisTemplate.expire(key, Duration.ofSeconds(WINDOW_DURATION));
//        }
//
//        // 3 > 3
//        if (requests > LIMIT) {
//            response.getWriter().write("Too Many Requests");
//            response.getWriter().flush();
//
//            return;
//        }


        chain.doFilter(request, response);
//        return; // exit
    }
}
