package com.login.auth.controllers;
import com.login.auth.utility.StringToRole;
import com.login.auth.model.User;
import com.login.auth.model.UserReqModel;
import com.login.auth.service.user.IUserService;
import com.login.auth.utility.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("users")
@Slf4j
public class UsersController {

    private final IUserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UsersController(IUserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("signup")
    public ResponseEntity addUser(@RequestBody UserReqModel userReqModel){
        StringToRole stringToRole = new StringToRole();
        stringToRole.setRoles(userReqModel.getRoles());

        if (userService.checkUser(userReqModel.getName())){
            log.info("username not valid");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        userService.createUser(new User(userReqModel.getName(), bCryptPasswordEncoder.encode(userReqModel.getPassword()),
                userReqModel.getEmail(), stringToRole.convertToRole(),null));
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @PostAuthorize("returnObject.name == principal.username")
    @GetMapping(path = "/{id}")
    public User Get(@PathVariable Long id){
        return userService.getUser(id);
    }


    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        if(userService.checkUserById(id)){
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PreAuthorize("#username == principal.username")
    @PutMapping(value = "/change/{username}")
    public ResponseEntity<?> change(@PathVariable String username,
                                    @RequestBody UserReqModel userReqModel){
        userService.changeUser(userReqModel.getName(),bCryptPasswordEncoder.encode(userReqModel.getPassword()),
                userReqModel.getEmail(),username);
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#username == principal.username")
    @GetMapping(value = "/profile/download/{username}")
    public ResponseEntity<?> downloadPhoto(@PathVariable String username, HttpServletResponse response) {
        FileUtil fileUtil = new FileUtil();
        try {
            fileUtil.downloadFile(username, response, userService.getPhotoByName(username));
        } catch (IOException e){
            log.error("error in downloading the file");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "profile/upload")
    public ResponseEntity<?> uploadPhoto(@RequestParam("image") MultipartFile multipartFile)  {
        FileUtil fileUtil = new FileUtil();
        if(fileUtil.uploadFile(userService.getName(), multipartFile.getOriginalFilename(), multipartFile)) {
            userService.savePhotoByName(multipartFile.getOriginalFilename(), SecurityContextHolder.getContext()
                    .getAuthentication().getName());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

}
