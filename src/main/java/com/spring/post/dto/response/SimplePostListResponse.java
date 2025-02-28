package com.spring.post.dto.response;

import java.util.List;

public record SimplePostListResponse(
    List<SimplePostResponse> postList,
    Long lastId
) {
}
