package com.spring.user.repository;

import static com.spring.fixture.UserFixture.createUser;
import static com.spring.user.domain.Role.ADMIN;
import static com.spring.user.domain.Role.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;

import com.spring.common.JpaConfig;
import com.spring.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class UserRepositoryTest extends JpaConfig {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저가 정상적으로 가입")
    void joinUser() {
        // given
        User memberUser = createUser("1@gmail.com", MEMBER);

        // when
        User savedUser = userRepository.save(memberUser);

        // then
        assertThat(savedUser).isEqualTo(memberUser);
    }

    @Test
    @DisplayName("멤버 역할 유저 정상적으로 검색")
    void getUserByMemberRole() {
        // given
        User memberUser1 = createUser("1@gmail.com", MEMBER);
        User memberUser2 = createUser("2@gmail.com", MEMBER);
        User adminUser = createUser("3@gmail.com", ADMIN);
        userRepository.saveAll(List.of(memberUser1, memberUser2, adminUser));

        // when
        List<User> findMemberUsers = userRepository.findAllByRole(MEMBER);

        // then
        assertThat(findMemberUsers).hasSize(2);
        assertThat(findMemberUsers).allMatch(user -> user.getRole() == MEMBER);
    }

    @Test
    @DisplayName("어드민 역할 유저 정상적으로 검색")
    void getUserByAdminRole() {
        // given
        User memberUser1 = createUser("1@gmail.com", MEMBER);
        User memberUser2 = createUser("2@gmail.com", MEMBER);
        User adminUser1 = createUser("3@gmail.com", ADMIN);
        User adminUser2 = createUser("4@gmail.com", ADMIN);
        userRepository.saveAll(List.of(memberUser1, memberUser2, adminUser1, adminUser2));

        // when
        List<User> findAdminUsers = userRepository.findAllByRole(ADMIN);

        // then
        assertThat(findAdminUsers).hasSize(2);
        assertThat(findAdminUsers).allMatch(user -> user.getRole() == ADMIN);
    }
}