package com.spring.user.dto.response;

public record SimpleUserResponse(
	Long userId,
	String email,
	String name,
	String role
) {
}
