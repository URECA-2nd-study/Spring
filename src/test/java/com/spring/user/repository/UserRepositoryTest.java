package com.spring.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.spring.common.config.QuerydslConfig;
import com.spring.common.exception.runtime.BaseException;
import com.spring.common.fixture.UserFixture;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.exception.UserErrorCode;
import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @ParameterizedTest
    @ValueSource(strings = {"어드민", "일반회원"})
    void 역할은_어드민과_일반회원_둘_중_하나여야_한다(String input) {
        // given
        User user1 = UserFixture.createUserByRole("abcd@gmail.com", Role.of(input));
        userRepository.save(user1);

        // when
        List<User> users = userRepository.findUserByRole(Role.of(input));

        // then
        assertThat(users).contains(user1);
    }

    @ParameterizedTest
    @ValueSource(strings = {"유효하지_않은_역할"})
    void 유효하지_않은_역할일_경우_예외가_발생해야_한다(String invalidRole) {
        assertThatThrownBy(() -> Role.of(invalidRole))
                .isInstanceOf(BaseException.class)
                .hasMessageContaining(UserErrorCode.INVALID_ROLE.getMessage());
    }
}