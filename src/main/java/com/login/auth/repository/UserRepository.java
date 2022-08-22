package com.login.auth.repository;

import com.login.auth.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    boolean existsUserByName(String name);
    User getUserByName(String name);
    User findUsersById(Long id);
    boolean existsUserById(Long id);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.name = :name, u.password = :password, u.email = :email WHERE u.name = :name2")
    void editUserByName(@Param("name") String name,@Param("password") String password,@Param("email")
                        String email,@Param("name2") String name2);
    @Query("SELECT u.photoName FROM User u WHERE u.name = :name")
    String getPhotoByName(@Param("name") String name);
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.photoName = :photoName WHERE u.name = :name")
    void savePhotoByName(@Param("name") String name, @Param("photoName") String photoName);
}
