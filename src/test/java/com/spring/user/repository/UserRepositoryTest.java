package com.spring.user.repository;

import com.spring.config.QueryDSLConfig;
import com.spring.fixture.UserFixture;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.dto.request.UserFilterSearchRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import(QueryDSLConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("필터링 검색 잘되는지 테스트")
    void filteredByRole() {

        //given
        User user1 = UserFixture.createUser("aaa@naver.com","1234","Jiyeon", Role.ADMIN);
        User user2 = UserFixture.createUser("bbb@gmail.com","abcd","Steve",Role.MEMBER);
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        UserFilterSearchRequest request = new UserFilterSearchRequest();
        request.setRole("ADMIN");
        List<User> result = userRepository.searchUsers(request);

        //then
        Assertions.assertThat(result).hasSize(1);
        Assertions.assertThat(result.get(0).getName()).isEqualTo("Jiyeon");

    }



}
