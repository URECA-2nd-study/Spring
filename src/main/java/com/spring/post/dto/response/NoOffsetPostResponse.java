package com.spring.post.dto.response;

import java.util.List;

public record NoOffsetPostResponse(

	List<SimplePostResponse> content,
	boolean hasNext
) {
}
