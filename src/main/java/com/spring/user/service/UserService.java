package com.spring.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.dto.request.FilteredUsersRequest;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.UpdateUserRequest;
import com.spring.user.dto.request.SimpleUserRequest;
import com.spring.user.dto.response.DeleteUserResponse;
import com.spring.user.dto.response.RegisterUserResponse;
import com.spring.user.dto.response.SimpleUserResponse;
import com.spring.user.domain.User;
import com.spring.user.dto.UserMapper;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public SimpleUserResponse getUser(SimpleUserRequest request) {
		User findUser = findUser(request.userId());

		return UserMapper.toSimpleUserResponse(findUser);
	}

	@Transactional(readOnly = true)
	public List<SimpleUserResponse> getUserAll() {
		List<User> findUserAll = userRepository.findAll();

		return UserMapper.toSimpleUserResponses(findUserAll);
	}

	@Transactional
	public RegisterUserResponse joinUser(RegisterUserRequest request) {
		User savedUser = userRepository.save(UserMapper.toUser(request));

		return UserMapper.toRegisterUserResponse(savedUser);
	}

	@Transactional
	public SimpleUserResponse updateUser(Long userId, UpdateUserRequest request) {
		validateEmail(request.email());

		User findUser = findUser(userId);

		findUser.updateInfo(request.email(), request.name(), request.role());

		return UserMapper.toSimpleUserResponse(findUser);
	}

	@Transactional
	public DeleteUserResponse deleteUser(Long userId) {
		User findUser = findUser(userId);

		userRepository.delete(findUser);
		return UserMapper.toDeleteUserResponse();
	}

	private User findUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new BaseException(UserErrorCode.NOT_FOUND_USER)
		);
	}

	private void validateEmail(String changingEmail) {
		if(userRepository.existsByEmail(changingEmail)) {
			throw new BaseException(UserErrorCode.DUPLICATED_EMAIL);
		}
	}

	public List<SimpleUserResponse> getFilteredUsers(FilteredUsersRequest request) {
		List<SimpleUserResponse> responses = userRepository.getFilteredUsers(request.role());

		if(responses.isEmpty()) {
			throw new BaseException(UserErrorCode.NOT_FOUND_FILTERED_USER);
		}

		return responses;
	}
}
