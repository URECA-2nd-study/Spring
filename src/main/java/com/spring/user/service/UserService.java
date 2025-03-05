package com.spring.user.service;

import com.spring.common.exception.runtime.BaseException;
import com.spring.post.service.PostService;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.dto.UserMapper;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.SimpleUserRequest;
import com.spring.user.dto.request.UpdateUserRequest;
import com.spring.user.dto.response.DeleteUserResponse;
import com.spring.user.dto.response.RegisterUserResponse;
import com.spring.user.dto.response.SimpleUserListResponse;
import com.spring.user.dto.response.SimpleUserResponse;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PostService postService;

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

	@Transactional(readOnly = true)
	public SimpleUserListResponse getUserByFilter(Role role) {
		List<User> findUserAll = userRepository.findAllByRole(role);

		return UserMapper.toSimpleUserListResponse(findUserAll);
	}

	// Required - 부모 트랜젝션 : 유저 저장 후 자동 포스트 생성
	@Transactional
	public RegisterUserResponse testRequiredA(RegisterUserRequest request) {
		// 유저를 저장
		User savedUser = userRepository.save(UserMapper.toUser(request));

		// 유저와 관련된 기본 post까지 저장
		try {
			postService.testRequiredB(savedUser.getId());
			System.out.println("생성 성공");
		} catch (Exception e) {
			System.out.println("POST ERROR");
		}

		return UserMapper.toRegisterUserResponse(savedUser);
	}

	// Requires_new - 부모 트랜젝션 : 유저 저장 후 자동 포스트 생성
	@Transactional
	public RegisterUserResponse testRequiresNewA(RegisterUserRequest request) {
		// 유저를 저장
		User savedUser = userRepository.save(UserMapper.toUser(request));

		// 유저와 관련된 기본 post까지 저장
		try {
			postService.testRequiresNewB(savedUser.getId());
			System.out.println("생성 성공");
		} catch (Exception e) {
			System.out.println("POST ERROR");
		}

		return UserMapper.toRegisterUserResponse(savedUser);
	}

}
