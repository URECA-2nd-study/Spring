package com.spring.common.exception.runtime;

public record ExceptionResponse(
	String errorCode,
	String message
) {
}
