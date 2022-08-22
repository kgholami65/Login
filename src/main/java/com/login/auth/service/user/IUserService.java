package com.login.auth.service.user;

import com.login.auth.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {
    void createUser(User user);
    boolean checkUser(String name);
    String getName();
    boolean checkUserById(Long Id);
    void deleteUser(Long id);
    User getUser(Long id);
    void changeUser(String name,String password,String email,String name2);
    String getPhotoByName(String name);
    void savePhotoByName(String photoName, String username);
}
