package com.spring.common.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserFixture {

    public static User createUser(String email, Role role){
        return User.of(
                email,
                "1234",
                "김원석",
                role);
    }
}
