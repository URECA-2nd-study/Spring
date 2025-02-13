package com.spring.post.dto.response;

public record SimplePostResponse(

	Long postId,
	String title,
	String content,
	String author
) {
}
