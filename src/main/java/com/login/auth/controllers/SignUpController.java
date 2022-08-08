package com.login.auth.controllers;
import com.login.auth.converter.StringToRole;
import com.login.auth.model.User;
import com.login.auth.model.UserReqModel;
import com.login.auth.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@Slf4j
public class SignUpController {

    private final IUserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StringToRole stringToRole;

    @Autowired
    public SignUpController(IUserService userService, BCryptPasswordEncoder bCryptPasswordEncoder,
                            StringToRole stringToRole) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.stringToRole = stringToRole;
    }

    @PostMapping("signup")
    public ResponseEntity addUser(@RequestBody UserReqModel userReqModel){
        stringToRole.setRoles(userReqModel.getRoles());

        if (userService.checkUser(userReqModel.getName())){
            log.info("username not valid");
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);
        }
        userService.createUser(new User(userReqModel.getName(),bCryptPasswordEncoder.encode(userReqModel.getPassword()),
                userReqModel.getEmail(),stringToRole.convertToRole()));
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

}
