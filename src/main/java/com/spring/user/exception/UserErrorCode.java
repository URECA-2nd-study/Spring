package com.spring.user.exception;

import com.spring.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

	NOT_FOUND_USER("해당 회원을 찾을 수 없습니다.", "USER_001"),
	DUPLICATED_EMAIL("중복된 이메일입니다.", "USER_002"),
	INVALID_ROLE("유효하지 않은 역할입니다.", "USER_003");

	private final String message;
	private final String code;
}
