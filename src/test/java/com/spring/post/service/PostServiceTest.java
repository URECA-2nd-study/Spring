package com.spring.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.spring.common.exception.runtime.BaseException;
import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.exception.PostErrorCode;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @DisplayName("게시글 정보를 업데이트 한다")
    @Test
    void updatePostTest() {
        // given
        Long postId = 1L;
        UpdatePostRequest request = new UpdatePostRequest(1L, "업데이트 제목", "업데이트 내용");

        User user = UserFixture.createUserByRole("abcde@gmail.com", Role.MEMBER);
        ReflectionTestUtils.setField(user, "id", 1L);
        Post post = PostFixture.createPost("새로운 제목", user);

        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(post));

        // when
        SimplePostResponse response = postService.updatePost(postId, request);

        // then
        assertAll(
                () -> verify(postRepository).findById(anyLong()),
                () -> assertThat(response.title().equals(request.title())),
                () -> assertThat(response.content().equals(request.content())),
                () -> assertThat(response.author().equals(user.getName()))
        );
    }

    @DisplayName("업데이트 할 게시글이 없을 때는 업데이트를 하지 않는다")
    @Test
    void doNotUpdatePostTest() {
        Long postId = 1L;
        UpdatePostRequest request = new UpdatePostRequest(1L, "업데이트 제목", "업데이트 내용");

        User user = UserFixture.createUserByRole("abcde@gmail.com", Role.MEMBER);
        ReflectionTestUtils.setField(user, "id", 1L);

        given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        assertThatThrownBy(
                () -> postService.updatePost(postId, request))
                .isInstanceOf(BaseException.class)
                .hasMessage(PostErrorCode.NOT_FOUND_POST.getMessage());
    }

}