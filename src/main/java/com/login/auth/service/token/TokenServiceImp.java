package com.login.auth.service.token;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenServiceImp implements ITokenService {

    private final RedisTemplate<String, String> redisTemplate;


    @Value("${token.expiration.time}")
    private long expirationTime;

    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.prefix}")
    private String tokenPrefix;



    @Autowired
    public TokenServiceImp(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void saveToken(String token, String username) {
        token = token.replace(tokenPrefix, "");
        redisTemplate.opsForValue().set(username, token);
        redisTemplate.expire(username, expirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean checkToken(String token, String username) {
        token = token.replace(tokenPrefix, "");
        String token2 = redisTemplate.opsForValue().get(username);
        if(token2 == null)
            return false;
        return token2.equals(token);
    }

    @Override
    public String generateToken(String userName) {
        return Jwts.builder().setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512 , tokenSecret)
                .compact();
    }

    @Override
    public String getUserName(String token) throws ExpiredJwtException{
        token = token.replace(tokenPrefix, "");
        return Jwts.parser().setSigningKey(tokenSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
