package com.spring.post.repository;

import com.spring.post.domain.Post;
import com.spring.post.dto.request.SimplePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest
@Transactional
@Commit
public class PostRepositoryTest {

    @Autowired
    private PostRepositoryImpl postRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    void before() {

        for (int i = 1; i <= 60; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            Post post = Post.of(sb.toString(), sb.toString(), null);
            em.persist(post);
        }
        em.flush();
        em.clear();
    }

    @Test
    void testSearchPost() {
        Long lastPostId = 50L;
        SimplePostRequest condition = new SimplePostRequest(lastPostId);

        Pageable pageable = PageRequest.of(0, 10);
        Slice<SimplePostResponse> result = postRepository.searchPost(condition, pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.hasNext()).isTrue();

        List<SimplePostResponse> content = result.getContent();
    }
}
