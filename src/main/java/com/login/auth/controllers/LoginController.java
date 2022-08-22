package com.login.auth.controllers;

import com.login.auth.model.LoginModel;
import com.login.auth.service.otp.IOTPService;
import com.login.auth.service.token.ITokenService;
import com.login.auth.service.user.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("authentication")
@Slf4j
public class LoginController {
    private final IUserService userService;
    private final AuthenticationManager authenticationManager;
    private final IOTPService otpService;
    private final ITokenService tokenService;


    @Autowired
    public LoginController(IUserService userService, AuthenticationManager authenticationManager,
                           IOTPService otpService, ITokenService tokenService){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.otpService = otpService;
        this.tokenService = tokenService;
    }


    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginModel loginModel){
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(loginModel.getName(),
                loginModel.getPassword());
        try {
            authenticationManager.authenticate(authenticationToken);
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
        String token = tokenService.generateToken(userService.getName());
        tokenService.saveToken(token, userService.getName());
        log.info("authentication successfull");
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
