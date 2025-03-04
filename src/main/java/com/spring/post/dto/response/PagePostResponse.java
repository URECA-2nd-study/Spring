package com.spring.post.dto.response;

import org.springframework.data.domain.Slice;

public record PagePostResponse (

    Slice<SimplePostResponse> slice,
    Long lastPostId
) {
}
