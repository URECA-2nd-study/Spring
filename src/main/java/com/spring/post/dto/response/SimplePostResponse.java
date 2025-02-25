package com.spring.post.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import com.spring.post.domain.Post;

public record SimplePostResponse(

	Long postId,
	String title,
	String content,
	String author
) {
	@QueryProjection
	public SimplePostResponse(
		Post post,
		String author
	) {
		this(
			post.getId(),
			post.getTitle(),
			post.getContent(),
			author
		);
	}
}
