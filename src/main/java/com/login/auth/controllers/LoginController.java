package com.login.auth.controllers;

import com.login.auth.model.LoginModel;
import com.login.auth.security.SecurityConstants;
import com.login.auth.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("authentication")
@Slf4j
public class LoginController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public LoginController(UserService userService,AuthenticationManager authenticationManager){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginModel loginModel, HttpServletResponse response){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginModel.getName(),
                    loginModel.getPassword()));
        } catch (DisabledException e){
            log.error("User disabled");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (BadCredentialsException e){
            log.error("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String token = Jwts.builder().setSubject(loginModel.getName())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512 , SecurityConstants.TOKEN_SECRET)
                .compact();
        log.info("authentication successful");
        response.addHeader(SecurityConstants.HEADER_STRING , SecurityConstants.TOKEN_PREFIX + token);
        return new ResponseEntity<>(SecurityConstants.TOKEN_PREFIX + token,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String get(){
        return "welcome " + userService.getName();
    }
}
