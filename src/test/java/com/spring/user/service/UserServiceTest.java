package com.spring.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    public UserService userService;

    @Autowired
    public UserServiceA userServiceA;

    @Test
    void required_test() {
        userServiceA.requiredA();
        assertThat(userService.getUserAll().size()).isEqualTo(0);
    }

    @Test
    void requires_new_test() {
        userServiceA.requiresNewA();
        assertThat(userService.getUserAll().size()).isEqualTo(1);
    }
}
