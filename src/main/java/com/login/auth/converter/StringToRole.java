package com.login.auth.converter;

import com.login.auth.model.Roles;
import com.login.auth.repository.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StringToRole {
    private RolesRepository repository;

    @Autowired
    public void setRepository(RolesRepository repository) {
        this.repository = repository;
    }

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
            repository.delete(roles2);
        }
        return roles1;
    }
}
