package com.login.auth.security;

import com.login.auth.service.IUserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {
    private final IUserService userService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, IUserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(SecurityConstants.HEADER_STRING);
        if(header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX) /*|| date.before(new Date())*/){
            chain.doFilter(request,response);
            return;
        }
        UsernamePasswordAuthenticationToken authenticationToken = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request,response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
        String token  = request.getHeader(SecurityConstants.HEADER_STRING);
        log.info("access token is: " + token);
        UserDetails userDetails = null;
        if(token != null){
            try {
                token = token.replace(SecurityConstants.TOKEN_PREFIX, "");
                String username = Jwts.parser().setSigningKey(SecurityConstants.TOKEN_SECRET)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
                try {
                    userDetails = userService.loadUserByUsername(username);
                } catch (UsernameNotFoundException e){
                    log.error(e.getMessage());
                    return null;
                }
                log.info(username);
                log.info(userDetails.getAuthorities().toString());
                if (username != null)
                    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            } catch (ExpiredJwtException e){
                log.error(e.getMessage());
            }
            return null;
        }
        return null;
    }
}
