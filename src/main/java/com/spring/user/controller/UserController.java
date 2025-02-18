package com.spring.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.UpdateUserRequest;
import com.spring.user.dto.response.DeleteUserResponse;
import com.spring.user.dto.response.RegisterUserResponse;
import com.spring.user.dto.response.SimpleUserResponse;
import com.spring.user.dto.UserMapper;
import com.spring.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(("/api/user"))
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	// 유저 단건 조회
	@GetMapping("/{userId}")
	public ResponseEntity<SimpleUserResponse> getUser(@PathVariable("userId") Long userId) {
		SimpleUserResponse response = userService.getUser(UserMapper.toSimpleUserRequest(userId));

		return ResponseEntity.ok(response);
	}

	// 유저 다건(전체) 조회
	@GetMapping("/")
	public ResponseEntity<List<SimpleUserResponse>> getUserAll() {
		List<SimpleUserResponse> response = userService.getUserAll();

		return ResponseEntity.ok(response);
	}

	// 유저 등록
	@PostMapping("/")
	public ResponseEntity<RegisterUserResponse> joinUser(@RequestBody RegisterUserRequest request) {
		RegisterUserResponse response = userService.joinUser(request);

		return ResponseEntity.ok(response);
	}

	// 유저 정보 업데이트
	@PatchMapping("/{userId}")
	public ResponseEntity<SimpleUserResponse> updateUser(
		@PathVariable("userId") Long userId,
		@RequestBody UpdateUserRequest request) {
		SimpleUserResponse response = userService.updateUser(userId, request);

		return ResponseEntity.ok(response);
	}

	// 유저 삭제
	@DeleteMapping("/{userId}")
	public ResponseEntity<DeleteUserResponse> deleteUser(
		@PathVariable("userId") Long userId) {
		DeleteUserResponse response = userService.deleteUser(userId);

		return ResponseEntity.ok(response);
	}

	// 역할로 유저 다건(전체) 조회
	@GetMapping("/filter")
	public ResponseEntity<List<SimpleUserResponse>> filterUserByRole(
			@RequestParam(required = false) String role) {
		System.out.println(role);

		List<SimpleUserResponse> response = userService.findAllUsersByRole(role);

		return ResponseEntity.ok(response);
	}
}
