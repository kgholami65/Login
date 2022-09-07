package com.login.auth.model;

import com.google.common.hash.HashCode;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

@Entity
//@Data
//@EqualsAndHashCode
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
    private Set<Roles> roles;

    @Column(name = "mobile_number", unique = true)
    private String mobileNumber;

    @Column(name = "photo_name")
    private String photoName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }


    public User(String name,String password,String email,Set<Roles> roles,String photoName, String mobileNumber,
                 LocalDateTime createdAt){
        this.name = name;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.photoName = photoName;
        this.mobileNumber = mobileNumber;
        this.createdAt = createdAt;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public User() {

    }
}
