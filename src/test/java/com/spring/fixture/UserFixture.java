package com.spring.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;

public class UserFixture {
    public static User createUser(String email, Role role){
        return User.of(
                email,
                "pwd",
                "테스트유저",
                role
        );
    }
}
