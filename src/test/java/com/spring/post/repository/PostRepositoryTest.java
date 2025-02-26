package com.spring.post.repository;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.spring.common.config.JpaConfig;
import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.response.NoOffsetPostResponse;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;

public class PostRepositoryTest extends JpaConfig {

	@Autowired
	PostRepository postRepository;

	@Autowired
	UserRepository userRepository;

	@DisplayName("no-offset 방식을 이용해 게시글 목록을 조회한다.")
	@Test
	void getPostsWithNoOffsetPagination() {
	    // given
		User author = UserFixture.user("test@navr.com", Role.MEMBER);
		userRepository.save(author);

		List<Post> posts = new ArrayList<>();
		for(int i = 1; i < 20; i++) {
			posts.add(PostFixture.post(String.valueOf(i), author));
		}
		postRepository.saveAll(posts);

		// when
		NoOffsetPostResponse response = postRepository.paginationPostWithNoOffset(posts.get(12).getId());

		// then
		List<SimplePostResponse> content = response.content();

		Assertions.assertTrue(response.hasNext());
		assertThat(content).hasSize(5)
			.extracting("postId")
			.containsExactlyInAnyOrder(
				posts.get(11).getId(),
				posts.get(10).getId(),
				posts.get(9).getId(),
				posts.get(8).getId(),
				posts.get(7).getId()
			);
	}

	@DisplayName("게시글의 다음 목록이 없으면 hasNext값은 false다.")
	@Test
	void hasNotNextPage() {
	    // given
		User author = UserFixture.user("test@navr.com", Role.MEMBER);
		userRepository.save(author);

		List<Post> posts = new ArrayList<>();
		for(int i = 1; i < 20; i++) {
			posts.add(PostFixture.post(String.valueOf(i), author));
		}
		postRepository.saveAll(posts);

		// when
		NoOffsetPostResponse response = postRepository.paginationPostWithNoOffset(posts.get(5).getId());

		// then
		Assertions.assertFalse(response.hasNext());
	}

	@DisplayName("")
	@Test
	void test() {
		// given
		User author = UserFixture.user("test@navr.com", Role.MEMBER);
		userRepository.save(author);

		List<Post> posts = new ArrayList<>();
		for(int i = 1; i < 20; i++) {
			posts.add(PostFixture.post(String.valueOf(i), author));
		}
		postRepository.saveAll(posts);

		// when
		NoOffsetPostResponse response = postRepository.paginationPostWithNoOffset(posts.get(12).getId());

		// then
		List<SimplePostResponse> content = response.content();

		Assertions.assertTrue(response.hasNext());
		assertThat(content).hasSize(5)
			.extracting("author")
			.containsExactlyInAnyOrder(
				posts.get(11).getUser().getName(),
				posts.get(10).getUser().getName(),
				posts.get(9).getUser().getName(),
				posts.get(8).getUser().getName(),
				posts.get(7).getUser().getName()
			);
	}
}
