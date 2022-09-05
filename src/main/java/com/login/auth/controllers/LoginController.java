package com.login.auth.controllers;

import com.login.auth.model.LoginModel;
import com.login.auth.model.VerifyMobileReq;
import com.login.auth.model.VerifyOtpReq;
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
import org.springframework.security.core.context.SecurityContextHolder;
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
        String token = tokenService.generateToken(loginModel.getName());
        tokenService.saveToken(token, loginModel.getName());

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String get(){
        return "welcome " + SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody VerifyMobileReq mobileReq){
        if(userService.checkUserByMobile(mobileReq.getMobile_number())){
            otpService.generateOTP(mobileReq.getUsername());
            return ResponseEntity.ok("head to http://localhost:8090/verify to authenticate");
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }



    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerifyOtpReq otpReq){
        boolean isValid = otpService.validateOTP(otpReq.getUsername(), otpReq.getOtp());
        if(!isValid){
            log.error("invalid code! please try again");
            return new ResponseEntity<>("invalid code! please try again", HttpStatus.NOT_ACCEPTABLE);
        }
        String token = tokenService.generateToken(otpReq.getUsername());
        tokenService.saveToken(token, otpReq.getUsername());
        log.info("authentication successfull");
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
