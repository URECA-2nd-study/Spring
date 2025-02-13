package com.spring.user.dto.request;

public record UpdateUserRequest(
	String email,
	String name,
	String role
) {
}
