package com.spring.user.repository;

import static com.spring.fixture.UserFixture.adminUser1;
import static com.spring.fixture.UserFixture.memberUser1;
import static com.spring.fixture.UserFixture.memberUser2;
import static org.assertj.core.api.Assertions.assertThat;

import com.spring.common.domain.QuerydslConfig;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저가 정상적으로 가입")
    void joinUser() {
        // given
        User memberUser = memberUser1();

        // when
        User savedUser = userRepository.save(memberUser);

        // then
        assertThat(savedUser).isEqualTo(memberUser);
    }

    @Test
    @DisplayName("멤버 역할 유저 정상적으로 검색")
    void getUserByMemberRole() {
        // given
        User memberUser1 = memberUser1();
        User memberUser2 = memberUser2();
        User adminUser = adminUser1();
        userRepository.saveAll(List.of(memberUser1, memberUser2, adminUser));

        // when
        List<User> findMemberUsers = userRepository.findAllByRole(Role.MEMBER);

        // then
        assertThat(findMemberUsers).hasSize(2);
        assertThat(findMemberUsers).allMatch(user -> user.getRole() == Role.MEMBER);
    }

    @Test
    @DisplayName("어드민 역할 유저 정상적으로 검색")
    void getUserByAdminRole() {
        // given
        User memberUser1 = memberUser1();
        User memberUser2 = memberUser2();
        User adminUser = adminUser1();
        userRepository.saveAll(List.of(memberUser1, memberUser2, adminUser));

        // when
        List<User> findAdminUsers = userRepository.findAllByRole(Role.ADMIN);

        // then
        assertThat(findAdminUsers).hasSize(2);
        assertThat(findAdminUsers).allMatch(user -> user.getRole() == Role.ADMIN);
    }
}