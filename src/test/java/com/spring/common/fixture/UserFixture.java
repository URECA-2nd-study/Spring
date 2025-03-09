package com.spring.common.fixture;

import java.math.BigDecimal;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserFixture {

	public static User user(String email, Role role) {
		return User.of(
			email,
			"123456",
			"김테스트",
			role
		);
	}

	public static User user(String email) {
		return User.of(
			email,
			"123456",
			"김테스트",
			Role.MEMBER,
			BigDecimal.ZERO
		);
	}
}
