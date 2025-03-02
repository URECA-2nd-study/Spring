package com.spring.post.service;

import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MockPostServiceTest {

    @Mock private PostRepository postRepository;
    @InjectMocks private PostService postService;


    @Test
    @DisplayName("포스트를 정상적으로 업데이트할 수 있다.")
    public void updatePostTest(){
        User user = UserFixture.createUser("abc@gmail.com", Role.ADMIN);
        Post post = PostFixture.createPost("test", user);

        ReflectionTestUtils.setField(post, "id", 1L);
        ReflectionTestUtils.setField(user, "id", 1L);

        UpdatePostRequest updatePostRequest = new UpdatePostRequest(user.getId(), "update", "update content");
        when(postRepository.findById(anyLong())).thenReturn(Optional.ofNullable(post));

        SimplePostResponse simplePostResponse = postService.updatePost(post.getId(), updatePostRequest);

        assertThat(simplePostResponse.postId()).isEqualTo(post.getId());
        assertThat(simplePostResponse.title()).isEqualTo(updatePostRequest.title());
        assertThat(simplePostResponse.content()).isEqualTo(updatePostRequest.content());
        assertThat(simplePostResponse.author()).isEqualTo(user.getName());
    }
}
