package com.spring.user.dto.request;

public record RegisterUserRequest(
	String email,
	String password,
	String name
) {
}
