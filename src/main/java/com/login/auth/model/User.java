package com.login.auth.model;

import lombok.Data;
import lombok.NonNull;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    @NonNull
    private String name;
    private String password;
    @Column(unique = true)
    @NonNull
    private String email;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")}
    )
    private Set<Roles> authorities;
    private String photoName;


    public User(String name,String password,String email,Set<Roles> authorities,String photoName){
        this.name = name;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.photoName = photoName;
    }


    public User(){}

}
