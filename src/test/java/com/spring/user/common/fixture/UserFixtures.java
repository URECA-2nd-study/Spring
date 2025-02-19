package com.spring.user.common.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class UserFixtures {
    // 역할마다 5명의 유저 생성
    public static List<User> createUsersForEachRole() {
        List<User> users = new ArrayList<>();
        int count = 1;
        for (Role role : Role.values()) {
            for (int j = 0; j < 5; j++) {
                users.add(User.of(count+"aaa@naver.com", count+"aaa1234", count+"aaa", role));
                count++;
            }
        }
        return users;
    }
}
