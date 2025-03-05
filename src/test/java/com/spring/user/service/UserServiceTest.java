package com.spring.user.service;

import com.spring.post.domain.Post;
import com.spring.post.repository.PostRepository;
import com.spring.user.common.fixture.UserFixtures;
import com.spring.user.domain.User;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @AfterEach
    void teardown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Required Test")
    void requiredTest() {
        // Given
        User user = UserFixtures.createUser();
        ReflectionTestUtils.setField(user, "id", 100L);

        //when
        try {
            userService.testRequiredA(new RegisterUserRequest(user.getEmail(), user.getPassword(), user.getName()));
        } catch (Exception e) {
            System.out.println("에러 발생");
        }

        // then
        List<User> userAll = userRepository.findAll();
        List<Post> postAll = postRepository.findAll();
        assertAll(
            () -> assertThat(userAll).hasSize(0),
            () -> assertThat(postAll).hasSize(0)
        );
    }

    @Test
    @DisplayName("Required Test")
    void requiresNewTest() {
        // Given
        User user = UserFixtures.createUser();
        ReflectionTestUtils.setField(user, "id", 100L);

        //when
        try {
            userService.testRequiresNewA(new RegisterUserRequest(user.getEmail(), user.getPassword(), user.getName()));
        } catch (Exception e) {
            System.out.println("에러 발생");
        }

        // then
        // 유저는 저장, Post는 미저장
        List<User> userAll = userRepository.findAll();
        List<Post> postAll = postRepository.findAll();
        assertAll(
            () -> assertThat(userAll).hasSize(1),
            () -> assertThat(postAll).hasSize(0)
        );
    }

    // mendatory : A는 @Transactional 안붙이기
    // B는 Mendatory -> user는 롤백안됨(Transaction아니니까), mendatory는 에러 발생
}
