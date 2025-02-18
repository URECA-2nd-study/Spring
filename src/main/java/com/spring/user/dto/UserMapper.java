package com.spring.user.dto;

import java.util.ArrayList;
import java.util.List;

import com.spring.user.domain.Role;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.SimpleUserRequest;
import com.spring.user.dto.response.DeleteUserResponse;
import com.spring.user.dto.response.RegisterUserResponse;
import com.spring.user.dto.response.SimpleUserResponse;
import com.spring.user.domain.User;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

	public static SimpleUserRequest toSimpleUserRequest(Long userId) {
		return new SimpleUserRequest(userId);
	}

	public static SimpleUserResponse toSimpleUserResponse(User user) {
		return new SimpleUserResponse(
			user.getId(),
			user.getEmail(),
			user.getName(),
			user.getRole().getRole()
		);
	}

	public static User toUser(RegisterUserRequest request) {
		return User.of(
			request.email(),
			request.password(),
			request.name(),
			Role.MEMBER
		);
	}

	public static RegisterUserResponse toRegisterUserResponse(User savedUser) {
		return new RegisterUserResponse(
			savedUser.getId(),
			savedUser.getEmail(),
			savedUser.getName(),
			savedUser.getRole().getRole()
		);
	}

	public static DeleteUserResponse toDeleteUserResponse() {
		return new DeleteUserResponse(true);
	}

	public static List<SimpleUserResponse> toSimpleUserResponses(List<User> findUserAll) {
		List<SimpleUserResponse> response = new ArrayList<>();
		findUserAll.stream().forEach(
			findUser -> response.add(new SimpleUserResponse(
				findUser.getId(),
				findUser.getEmail(),
				findUser.getName(),
				findUser.getRole().getRole()
			))
		);

		return response;
	}
}
