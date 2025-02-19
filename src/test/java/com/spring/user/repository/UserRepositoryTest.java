package com.spring.user.repository;

import static com.mysema.commons.lang.Assert.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.spring.common.config.JpaConfig;
import com.spring.common.config.QuerydslConfig;
import com.spring.common.fixture.UserFixture;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.dto.response.SimpleUserResponse;


public class UserRepositoryTest extends JpaConfig {

	@Autowired
	private UserRepository 	userRepository;

	@DisplayName("[역할군 필터에 맞는 회원을 조회한다.]")
	@Test
	void selectUserForFilteredRole() {
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

		// whenR
		List<SimpleUserResponse> members = userRepository.getFilteredUsers("일반회원");

		// then

		assertThat(members).hasSize(4)
			.extracting("email", "role")
			.containsExactlyInAnyOrder(
				tuple("test2@test.com", Role.MEMBER.getRole()),
				tuple("test4@test.com", Role.MEMBER.getRole()),
				tuple("test6@test.com", Role.MEMBER.getRole()),
				tuple("test8@test.com", Role.MEMBER.getRole())
			);
	}
}
