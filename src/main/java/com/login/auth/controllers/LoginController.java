package com.login.auth.controllers;

import com.login.auth.model.LoginModel;
import com.login.auth.security.SecurityConstants;
import com.login.auth.service.IOTPService;
import com.login.auth.service.IUserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@RequestMapping("authentication")
@Slf4j
public class LoginController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final IOTPService otpService;


    @Autowired
    public LoginController(IUserService userService, AuthenticationManager authenticationManager,
                           IOTPService otpService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.otpService = otpService;
    }


    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginModel loginModel){
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginModel.getName(),
                loginModel.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
        } catch (DisabledException e){
            log.error("User disabled");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (BadCredentialsException e){
            log.error("User not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        otpService.generateOTP(loginModel.getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String get(){
        return "welcome " + userService.getName();
    }



    @PostMapping("/verify/{otpNum}")
    public ResponseEntity<String> verify(@PathVariable int otpNum){
        Integer otp = otpNum;
        boolean isValid = otpService.validateOTP(userService.getName(), otp);
        if(!isValid){
            log.error("invalid code! please try again");
            return new ResponseEntity<>("invalid code! please try again", HttpStatus.NOT_ACCEPTABLE);
        }
        String token = Jwts.builder().setSubject(userService.getName())
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512 , SecurityConstants.TOKEN_SECRET)
                .compact();
        log.info("authentication successfull");
        return new ResponseEntity<>(SecurityConstants.TOKEN_PREFIX + token, HttpStatus.OK);
    }
}
