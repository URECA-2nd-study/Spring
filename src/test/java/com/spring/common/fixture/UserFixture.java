package com.spring.common.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;

public class UserFixture {

    public static User createUserByPassword(String email, String password, String name, Role role) {
        return User.of(email, password, name, role);
    }

    public static User createUserByRole(String email, Role role) {
        return User.of(email, "abc123", "김이름", role);
    }
}
