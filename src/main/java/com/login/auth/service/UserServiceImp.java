package com.login.auth.service;

import com.login.auth.model.Roles;
import com.login.auth.model.User;
import com.login.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImp implements UserService{
    private final UserRepository userRepository;
    private Long id;

    @Autowired
    public UserServiceImp(UserRepository userRepository){
        this.userRepository = userRepository;
    }
    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public boolean checkUser(String name) {
        return userRepository.existsUserByName(name);
    }

    @Override
    public String getName() {
        User user = userRepository.findUsersById(id);
        return user.getName();
    }

    @Override
    public boolean checkUserById(Long Id) {
        return userRepository.existsUserById(Id);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(userRepository.findUsersById(id));
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findUsersById(id);
    }

    @Override
    public void changeUser(String name, String password, String email, String name2) {
        userRepository.editUserByName(name,password,email,name2);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("name is " + username);
        User user = userRepository.getUserByName(username);
        if(user == null) {
            log.info("user is null");
            throw new UsernameNotFoundException("username not valid");
        }
        id = user.getId();
        Set<Roles> roles = user.getAuthorities();
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Roles role : roles)
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        return new org.springframework.security.core.userdetails.User(user.getName(),user.getPassword(),authorities);
    }


}
