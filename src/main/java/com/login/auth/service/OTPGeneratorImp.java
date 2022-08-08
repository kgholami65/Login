package com.login.auth.service;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
public class OTPGeneratorImp implements IOTPGenerator {
    private static final Integer EXPIRE_MIN = 4;
    private LoadingCache<String,Integer> otpCache;

    public OTPGeneratorImp(){
        super();
        otpCache = CacheBuilder.newBuilder().expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                .build(new CacheLoader<String, Integer>() {
                    @Override
                    public Integer load(String s) throws Exception {
                        return 0;
                    }
                });
    }

    @Override
    public Integer generateOTP(String key) {
        Random random =  new Random();
        Integer OTP = 1000 + random.nextInt(9000);
        otpCache.put(key, OTP);
        return OTP;
    }

    @Override
    public Integer getOTP(String key) {
        return otpCache.getIfPresent(key);
    }

    @Override
    public void clearOTP(String key) {
        otpCache.invalidate(key);
    }


}
