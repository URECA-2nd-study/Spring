package com.spring.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

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

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

	@Mock
	PostRepository postRepository;

	@InjectMocks
	PostService postService;

	@DisplayName("게시글 정보를 업데이트한다.")
	@Test
	void updatePost() {
	    // given
		Long postId = 1L;
		UpdatePostRequest request = new UpdatePostRequest(1L, "업데이트 글", "내용을 업데이트 합니다.");

		User user = UserFixture.user("test@naver.com", Role.MEMBER);
		ReflectionTestUtils.setField(user, "id", 1L);

		Post post = PostFixture.post("테스트 글", user);

		// mockito에게 명령하는 명령문
		// 너 given()이 주어질 때, willReturn()을 반환해. 무조건 항상 실제가 어떻게 되든지 상관 없이!!!!!
		given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(post));

		// when
		SimplePostResponse response = postService.updatePost(postId, request);

		// then
		assertAll(
			() -> verify(postRepository).findById(anyLong()),
			() -> assertThat(response.title()).isEqualTo(request.title()),
			() -> assertThat(response.content()).isEqualTo(request.content()),
			() -> assertThat(response.author()).isEqualTo(user.getName())
		);
	}

	@DisplayName("업데이트 할 게시글이 존재하지 않다면 업데이트가 이루어지지 않는다.")
	@Test
	void doNotUpdatePostWhenIsNotPost() {
	    // given
		Long postId = 1L;
		UpdatePostRequest request = new UpdatePostRequest(1L, "업데이트 글", "내용을 업데이트 합니다.");

		given(postRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

		// when		// then
		assertThatThrownBy(
			() -> postService.updatePost(postId, request))
			.isInstanceOf(BaseException.class)
			.hasMessage(PostErrorCode.NOT_FOUND_POST.getMessage());
	}
}
