package com.login.auth.service;

public interface IOTPGenerator {
    Integer generateOTP(String key);
    Integer getOTP(String key);
    void clearOTP(String key);
}
