package com.spring.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;

@SpringBootTest
class RealPostServiceTest {

	@Autowired
	UserRepository userRepository;

	@Autowired
	PostRepository postRepository;

	@Autowired
	PostService postService;

	@AfterEach
	void tearDown() {
		postRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("")
	@Test
	void realUpdatePost() {
	    // given
		User savedUser = userRepository.save(UserFixture.user("test@naver.com", Role.MEMBER));
		Post savedPost = postRepository.save(PostFixture.post("테스트 글", savedUser));

		UpdatePostRequest request = new UpdatePostRequest(savedUser.getId(), "업데이트 글", "내용을 업데이트 합니다.");

		// when
		SimplePostResponse response = postService.updatePost(savedPost.getId(), request);

		// then
		assertAll(
			() -> assertThat(response.title()).isEqualTo(request.title()),
			() -> assertThat(response.content()).isEqualTo(request.content()),
			() -> assertThat(response.author()).isEqualTo(savedUser.getName())
		);

	}
}
