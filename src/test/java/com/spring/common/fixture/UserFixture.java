package com.spring.common.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.math.BigDecimal;

public class UserFixture {

    public static User createUserByEmail(String email) {
        return User.of(email, "abcdefg", "박이름", Role.MEMBER, BigDecimal.ZERO);
    }

    public static User createUserByPassword(String email, String password, String name, Role role) {
        return User.of(email, password, name, role);
    }

    public static User createUserByRole(String email, Role role) {
        return User.of(email, "abc123", "김이름", role);
    }

    public static User createUserByPoint(BigDecimal point) {
        return User.of("abc@gmail.com", "abcdefg", "박이름", Role.MEMBER, point);
    }
}
