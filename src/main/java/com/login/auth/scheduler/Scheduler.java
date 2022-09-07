package com.login.auth.scheduler;

import com.login.auth.model.User;
import com.login.auth.repository.IProductRepository;
import com.login.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;

@Component
@Slf4j
public class Scheduler {

    private UserRepository userRepository;
    private IProductRepository productRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setProductRepository(IProductRepository productRepository){
        this.productRepository = productRepository;
    }


    @Scheduled(fixedRate = 30000) //count  all users every 10 seconds
    public void userReport(){
        ArrayList<User> users = (ArrayList<User>) userRepository.findAll();
        log.info("number of all users: {}", users.size());
    }


    @Scheduled(fixedDelay = 3600000) // count new users every hour
    public void countUsers(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime creationDate = now.minusHours(1);
        log.info("{} users have been added during one hour", userRepository.countUsersWithCreationDateAfter(creationDate));
    }

    @Scheduled(cron = "0 15 10 * * ?") // count new products every day
    public void countNewProducts(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime creationDate = now.minusSeconds(30);
        log.info("{} products have been added during 1 day", productRepository.countAllByCreatedAtAfter(creationDate));
    }


}
