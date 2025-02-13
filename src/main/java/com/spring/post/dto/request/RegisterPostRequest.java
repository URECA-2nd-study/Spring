package com.spring.post.dto.request;

public record RegisterPostRequest(
	String title,
	String content,
	Long userId
) {
}
