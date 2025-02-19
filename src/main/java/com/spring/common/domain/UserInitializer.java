package com.spring.common.domain;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements ApplicationRunner {

    private static final int ADMIN_CNT = 10;
    private static final int MEMBER_CNT = 10;

    private final UserRepository userRepository;

    public UserInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (userRepository.count() == 0) {
            userRepository.saveAll(createAdminUsers());
            userRepository.saveAll(createMemberUsers());
        }
    }

    private List<User> createAdminUsers() {
        List<User> adminUsers = new ArrayList<>();

        for (int i = 1; i <= ADMIN_CNT; i++) {
            adminUsers.add(createUser(i, Role.ADMIN));
        }
        return adminUsers;
    }

    private List<User> createMemberUsers() {
        List<User> memberUsers = new ArrayList<>();

        for (int i = ADMIN_CNT + 1; i <= ADMIN_CNT + MEMBER_CNT; i++) {
            memberUsers.add(createUser(i, Role.MEMBER));
        }
        return memberUsers;
    }

    private User createUser(int index, Role role) {
        String email = index + "@gmail.com";
        String password = "password" + index;
        String name = "user" + index;

        return User.of(email, password, name, role);
    }
}
