package com.login.auth.model;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class VerifyMobileReq {
    @NotNull
    private String mobile_number;

    @NotNull
    private String username;
}
