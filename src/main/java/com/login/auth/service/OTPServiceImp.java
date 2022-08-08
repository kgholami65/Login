package com.login.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OTPServiceImp implements IOTPService{
    private final IOTPGenerator otpGenerator;

    @Autowired
    public OTPServiceImp(IOTPGenerator otpGenerator) {
        this.otpGenerator = otpGenerator;
    }

    @Override
    public void generateOTP(String key) {
        log.info("your code is : {}" , otpGenerator.generateOTP(key));
    }

    @Override
    public boolean validateOTP(String key, Integer otpNum) {
        Integer otpValue = otpGenerator.getOTP(key);
        if(otpValue != null && otpValue.equals(otpNum)){
            otpGenerator.clearOTP(key);
            return true;
        }
        return false;
    }
}
