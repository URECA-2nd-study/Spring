package com.spring.post.dto;

import com.spring.post.domain.Post;
import com.spring.post.dto.request.SimplePostRequest;
import com.spring.post.dto.response.DeletePostResponse;
import com.spring.post.dto.response.SimplePostListResponse;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PostMapper {

	public static SimplePostRequest toSimplePostRequest(Long postId) {
		return new SimplePostRequest(postId);
	}

	public static SimplePostResponse toSimplePostResponse(Post post, String author) {
		return new SimplePostResponse(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			author
		);
	}

	public static Post toPost(String title, String content, User findUser) {
		return Post.of(title, content, findUser);
	}

	public static List<SimplePostResponse> toSimplePostResponses(List<Post> posts) {
		List<SimplePostResponse> response = new ArrayList<>();

		posts.stream().forEach(
			post -> response.add(new SimplePostResponse(
				post.getId(),
				post.getTitle(),
				post.getContent(),
				post.getUser().getName()
			))
		);

		return response;
	}

	public static DeletePostResponse toDeletePostResponse() {
		return new DeletePostResponse(true);
	}

	public static SimplePostListResponse toSimplePostListResponse(List<Post> findPostPage, Long lastPostId) {
		List<SimplePostResponse> postList = toSimplePostResponses(findPostPage);


		return new SimplePostListResponse(postList, lastPostId);
	}
}
