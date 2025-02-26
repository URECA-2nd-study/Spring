package com.spring.post.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private MockMvc mockMvc;

	@AfterEach
	void tearDown() {
		postRepository.deleteAllInBatch();
		userRepository.deleteAllInBatch();
	}

	@DisplayName("게시글 업데이트 API를 호출해 게시글을 업데이트 한다.")
	@Test
	void updatePostAPI() throws Exception {
	    // given
		User savedUser = userRepository.save(UserFixture.user("test@email.com", Role.MEMBER));
		Post savedPost = postRepository.save(PostFixture.post("수정전 제목", savedUser));

		UpdatePostRequest request = new UpdatePostRequest(savedUser.getId(), "수정된 제목", "수정된 내용");
		// when		// then
		mockMvc.perform(patch("/api/post/{postId}", savedPost.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(savedPost.getId()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.title").value(request.title()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.content").value(request.content()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.author").value(savedUser.getName()));
	}


	@DisplayName("게시글 주인이 아니라면 게시글을 업데이트 할 수 없다.")
	@Test
	void updatePostFailsForNonOwner() throws Exception {
		// given
		User savedUser = userRepository.save(UserFixture.user("test@email.com", Role.MEMBER));
		User nonOwner = userRepository.save(UserFixture.user("test2@email.com", Role.MEMBER));
		Post savedPost = postRepository.save(PostFixture.post("수정전 제목", savedUser));

		UpdatePostRequest request = new UpdatePostRequest(nonOwner.getId(), "수정된 제목", "수정된 내용");
		// when		// then
		mockMvc.perform(patch("/api/post/{postId}", savedPost.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
}
