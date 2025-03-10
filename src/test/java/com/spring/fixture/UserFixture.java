package com.spring.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.math.BigDecimal;

public class UserFixture {
    public static User createUser(String email, Role role){
        return User.of(
                email,
                "pwd",
                "테스트유저",
                role
        );
    }

    public static User createUser(String email, String password, String name, Role role){
        return User.of(
                email,
                password,
                name,
                role
        );
    }

    public static User createUser(String email, Role role, BigDecimal point){
        return User.of(
                email,
                "pwd",
                "테스트유저",
                role,
                point
        );
    }
}
