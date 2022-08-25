package com.login.auth.security;

import com.login.auth.service.token.ITokenService;
import com.login.auth.service.user.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final IUserService userService;
    private final ITokenService tokenService;



    @Autowired
    public AuthorizationFilter(AuthenticationManager authenticationManager, IUserService userService,
                               ITokenService tokenService) {
        super(authenticationManager);
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if(header == null){
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token  = request.getHeader("Authorization");
        log.info("access token is: " + token);
        UserDetails userDetails = null;
        try {
            String username = tokenService.getUserName(token);
            log.info(username);
            if(!tokenService.checkToken(token, username))
                throw new RuntimeException("token is not valid");
            try {
                userDetails = userService.loadUserByUsername(username);
            } catch (UsernameNotFoundException e){
                log.error(e.getMessage());
                return null;
            }
            log.info(userDetails.getAuthorities().toString());
            if (username != null)
                return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(),
                        userDetails.getAuthorities());
        } catch (ExpiredJwtException e){
            log.error(e.getMessage());
        }
        return null;
    }
}
