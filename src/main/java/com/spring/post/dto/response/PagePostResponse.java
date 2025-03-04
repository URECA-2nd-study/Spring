package com.spring.post.dto.response;

import com.spring.post.domain.Post;
import java.util.List;

public record PagePostResponse(
        List<SimplePostResponse> posts,
        boolean hasNext
) {
}
