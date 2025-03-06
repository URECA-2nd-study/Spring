package com.spring.post.repository;

import com.spring.common.config.JpaConfig;
import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.SimplePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@Transactional
public class PostRepositoryTest extends JpaConfig {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        List<Post> posts = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 60; i++) {
            users.add(UserFixture.createUser("abc@gmail.com "+ String.valueOf(i), Role.ADMIN));
        }

        for (int i = 1; i <= 60; i++) {
            posts.add(PostFixture.createPost(String.valueOf(i), users.get(i-1)));
        }

        userRepository.saveAll(users);
        postRepository.saveAll(posts);
    }

    @Test
    @DisplayName("게시글을 목록을 정상적으로 조회할 수 있다.")
    public void testSearchPostPages() {
        Long lastPostId = 60L;
        Pageable pageable = PageRequest.of(60, 10);
        Slice<Post> result = postRepository.searchPostPages(lastPostId, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.hasNext()).isTrue();

    }
}
