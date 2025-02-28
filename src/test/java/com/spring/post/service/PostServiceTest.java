package com.spring.post.service;

import com.spring.post.common.fixture.PostFixtures;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.repository.PostRepository;
import com.spring.user.common.fixture.UserFixtures;
import com.spring.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("Mockito: Post 갱신 Test")
    void updatePostWithMockTest() {
        // given
        User user = UserFixtures.createUser();
        ReflectionTestUtils.setField(user, "id", 1L);
        Post post = PostFixtures.createPost(user);
        UpdatePostRequest req = new UpdatePostRequest(1L, "제목입니다", "내용입니다");

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        // when
        SimplePostResponse newPost = postService.updatePost(post.getId(), req);
        System.out.println(newPost.title() + " " + newPost.content());

        // then
        Assertions.assertThat(newPost.title()).isEqualTo(req.title());
        Assertions.assertThat(newPost.content()).isEqualTo(req.content());
    }

}

