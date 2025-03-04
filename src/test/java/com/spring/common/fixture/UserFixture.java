package com.spring.common.fixture;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserFixture {

	public static User user(String email, Role role) {
		return User.of(
			email,
			"123456",
			"김테스트",
			role
		);
	}
}
