package com.spring.post.dto.response;

import java.util.List;

public record GetPostPageResponse(
        List<SimplePostResponse> postList,
        boolean hasNext
) {
}
