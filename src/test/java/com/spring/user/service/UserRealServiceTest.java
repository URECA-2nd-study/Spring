package com.spring.user.service;


import static com.spring.fixture.UserFixture.createUser;
import static com.spring.user.domain.Role.ADMIN;
import static com.spring.user.domain.Role.MEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.dto.response.SimpleUserResponse;
import com.spring.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserRealServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("멤버 역할 유저를 정상적으로 조회한다")
    public void filterMemberUser() {
        // given
        User memberUser1 = createUser("1@gmail.com", MEMBER);
        User memberUser2 = createUser("2@gmail.com", MEMBER);
        User adminUser1 = createUser("3@gmail.com", ADMIN);
        User adminUser2 = createUser("4@gmail.com", ADMIN);

        userRepository.saveAll(List.of(memberUser1, memberUser2, adminUser1, adminUser2));

        // when
        List<SimpleUserResponse> responses = userService.filterUserByRole(Role.MEMBER);

        // then
        assertEquals(2, responses.size());
        assertTrue(responses.stream().allMatch(user -> user.role().equals(MEMBER.getRole())));
    }

    @Test
    @DisplayName("어드민 역할 유저를 정상적으로 조회한다")
    public void filterAdminUser() {
        // given
        User memberUser1 = createUser("1@gmail.com", MEMBER);
        User memberUser2 = createUser("2@gmail.com", MEMBER);
        User adminUser1 = createUser("3@gmail.com", ADMIN);
        User adminUser2 = createUser("4@gmail.com", ADMIN);

        userRepository.saveAll(List.of(memberUser1, memberUser2, adminUser1, adminUser2));

        // when
        List<SimpleUserResponse> responses = userService.filterUserByRole(ADMIN);

        // then
        assertEquals(2, responses.size());
        assertTrue(responses.stream().allMatch(user -> user.role().equals(ADMIN.getRole())));
    }
}