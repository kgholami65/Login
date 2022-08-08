package com.login.auth.service;

public interface IOTPService {
    void generateOTP(String key);
    boolean validateOTP(String key, Integer otpNum);
}
