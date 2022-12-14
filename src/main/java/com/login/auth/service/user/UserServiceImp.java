package com.login.auth.service.user;


import com.login.auth.model.User;
import com.login.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImp implements IUserService {
    private final UserRepository userRepository;

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
    public String getPhotoByName(String name) {
        return userRepository.getPhotoByName(name);
    }

    @Override
    public void savePhotoByName(String photoName, String username) {
        userRepository.savePhotoByName(username, photoName);
    }

    @Override
    public boolean checkUserByMobile(String mobileNumber) {
        return userRepository.existsUserByMobileNumber(mobileNumber);
    }

    @Override
    public void editPasswordByName(String password, String username) {
        if(userRepository.existsUserByName(username))
            userRepository.editPasswordByName(password, username);
        else
            throw new UsernameNotFoundException("username not valid");
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("name is " + username);
        User user = userRepository.getUserByName(username);
        if(user == null) {
            log.info("user is null");
            throw new UsernameNotFoundException("username not valid");
        }
        List<GrantedAuthority> authorities = user.getRoles().stream().map(
                roles -> new SimpleGrantedAuthority(roles.getName())).collect(Collectors.toList());
        /*List<Authorities> authoritiesEntity = new ArrayList<>();
        user.getRoles().forEach(role -> authoritiesEntity.addAll(role.getAuthorities()));
        authoritiesEntity.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority.getName())));
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Roles role : roles)
            authorities.add(new SimpleGrantedAuthority(role.getName()));*/
        return new org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), authorities);
    }
}
