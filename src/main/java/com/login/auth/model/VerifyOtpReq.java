package com.login.auth.model;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class VerifyOtpReq {
    @NotNull
    private String username;

    @NotNull
    private Integer otp;

}
