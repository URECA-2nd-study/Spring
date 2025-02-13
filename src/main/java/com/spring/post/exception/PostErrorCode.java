package com.spring.post.exception;

import com.spring.common.exception.runtime.ErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostErrorCode implements ErrorCode {

	NOT_FOUND_POST("해당 게시글을 찾을 수 없습니다.", "POST_001"),
	POST_FORBIDDEN("게시글 권한이 없습니다.", "POST_002");

	private final String message;
	private final String code;
}
