package com.spring.user.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.common.fixture.UserFixture;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.dto.request.FilteredUsersRequest;
import com.spring.user.dto.response.SimpleUserResponse;
import com.spring.user.repository.UserRepository;

@SpringBootTest
public class UserServiceTest {


	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@DisplayName("[필터링에 일치하는 역할을 가진 회원을 가져온다]")
	@Test
	void getUsersForFiltered() {
	    // given
		User 김테스트1 = UserFixture.user("test1@test.com", Role.ADMIN);
		User 김테스트2 = UserFixture.user("test2@test.com", Role.MEMBER);
		User 김테스트3 = UserFixture.user("test3@test.com", Role.ADMIN);
		User 김테스트4 = UserFixture.user("test4@test.com", Role.MEMBER);
		User 김테스트5 = UserFixture.user("test5@test.com", Role.ADMIN);
		User 김테스트6 = UserFixture.user("test6@test.com", Role.MEMBER);
		User 김테스트7 = UserFixture.user("test7@test.com", Role.ADMIN);
		User 김테스트8 = UserFixture.user("test8@test.com", Role.MEMBER);

		userRepository.saveAll(List.of(김테스트1, 김테스트2, 김테스트3, 김테스트4, 김테스트5, 김테스트6, 김테스트7, 김테스트8));

		FilteredUsersRequest request = new FilteredUsersRequest("어드민 ");

		// when
		List<SimpleUserResponse> responses = userService.getFilteredUsers(request);

		// then
		assertThat(responses).hasSize(4)
			.extracting("email", "role")
			.containsExactlyInAnyOrder(
				tuple("test1@test.com", Role.ADMIN.getRole()),
				tuple("test3@test.com", Role.ADMIN.getRole()),
				tuple("test5@test.com", Role.ADMIN.getRole()),
				tuple("test7@test.com", Role.ADMIN.getRole())
			);

	}
}
