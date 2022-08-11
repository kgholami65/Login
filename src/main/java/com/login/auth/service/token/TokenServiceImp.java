package com.login.auth.service.token;

import com.login.auth.security.SecurityConstants;
import com.login.auth.service.token.ITokenService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class TokenServiceImp implements ITokenService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TokenServiceImp(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public void saveToken(String token, String username) {
        redisTemplate.opsForValue().set(username, token);
        redisTemplate.expire(username, SecurityConstants.EXPIRATION_TIME, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean checkToken(String token, String username) {
        String token2 = redisTemplate.opsForValue().get(username);
        if(token2 == null)
            return false;
        return token2.equals(token);
    }

    @Override
    public String generateToken(String userName) {
        return Jwts.builder().setSubject(userName)
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512 , SecurityConstants.TOKEN_SECRET)
                .compact();
    }

    @Override
    public String getUserName(String token) throws ExpiredJwtException{
        return Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
