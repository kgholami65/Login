package com.login.auth.model;

import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class UserReqModel {
    private String name;
    private String password;
    private String email;
    private Set<String> roles;
    private String mobile_number;
}
