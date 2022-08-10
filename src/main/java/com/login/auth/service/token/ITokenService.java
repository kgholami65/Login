package com.login.auth.service.token;

import org.springframework.data.redis.core.RedisTemplate;

public interface ITokenService {
    void saveToken(String token);
    boolean checkToken(String token);
    String generateToken(String userName);
    String getUserName(String token);
}
