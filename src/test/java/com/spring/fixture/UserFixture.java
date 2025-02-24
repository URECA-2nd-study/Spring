package com.spring.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;

public class UserFixture {
    public static User memberUser1() {
        return User.of(
                "member1@gamil.com",
                "pwd",
                "memberUser1",
                Role.MEMBER
        );
    }

    public static User memberUser2() {
        return User.of(
                "member2@gamil.com",
                "pwd",
                "memberUser2",
                Role.MEMBER
        );
    }

    public static User adminUser1() {
        return User.of(
                "admin1@gmail.com",
                "pwd",
                "adminUser1",
                Role.ADMIN
        );
    }
}
