package com.spring.user.dto.response;

public record RegisterUserResponse(
	Long userId,
	String email,
	String name,
	String role
) {
}
