package com.login.auth.utility;

import com.login.auth.model.Roles;
import com.login.auth.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

public class StringToRole {

    private Set<String> roles;

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Set<Roles> convertToRole(){
        Set<Roles> roles1 = new HashSet<>();
        for(String name : roles){
            Roles roles2 = new Roles();
            roles2.setName(name);
            roles1.add(roles2);
        }
        return roles1;
    }
}
