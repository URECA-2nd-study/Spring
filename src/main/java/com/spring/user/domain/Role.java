package com.spring.user.domain;

import java.util.Arrays;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.exception.UserErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	ADMIN("어드민"),
	MEMBER("일반회원");

	private final String role;

	public static Role of(String role) {
		return Arrays.stream(values())
			.filter(r -> r.role.equals(role))
			.findAny()
			.orElseThrow(() -> new BaseException(UserErrorCode.INVALID_ROLE));
	}
}
