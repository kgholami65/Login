package com.login.auth.service.token;

import org.springframework.data.redis.core.RedisTemplate;

public interface ITokenService {
    void saveToken(String token, String username);
    boolean checkToken(String token, String username);
    String generateToken(String username);
    String getUserName(String token);
}
