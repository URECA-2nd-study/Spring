package com.spring.common.domain;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;

    @Autowired
    public DataInitializer(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.count() == 0){
            userRepository.saveAll(Arrays.asList(
                    User.of("1@email.com","password","user1", Role.ADMIN),
                    User.of("2@email.com","password","user2", Role.ADMIN),
                    User.of("3@email.com","password","user4", Role.MEMBER),
                    User.of("4@email.com","password","user1", Role.MEMBER)
            ));
        }
    }
}
