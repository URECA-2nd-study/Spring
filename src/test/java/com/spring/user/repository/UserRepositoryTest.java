package com.spring.user.repository;

import com.spring.common.config.QueryDslConfig;
import com.spring.user.common.fixture.UserFixtures;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(QueryDslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("필터링 기능 검사")
    void findAllByRoleTest() {
        // given
        List<User> users = UserFixtures.createUsersForEachRole();
        userRepository.saveAll(users);

        // when, then
        for (Role role : Role.values()) {
            // when
            List<User> foundUsers = userRepository.findAllByRole(role);

            // then
            foundUsers.stream().map(u -> Assertions.assertThat(u.getRole().equals(role)));
        }
        Assertions.assertThat(users.size()).isEqualTo(userRepository.findAll().size());
    }
}
