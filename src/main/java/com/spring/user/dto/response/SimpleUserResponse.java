package com.spring.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.user.domain.User;

public record SimpleUserResponse(
	Long userId,
	String email,
	String name,
	String role
) {

	@QueryProjection
	public SimpleUserResponse(
		User user) {
		this(
			user.getId(),
			user.getEmail(),
			user.getName(),
			user.getRole().getRole()
		);
	}
}
