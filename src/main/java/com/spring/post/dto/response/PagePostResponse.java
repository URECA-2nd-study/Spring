package com.spring.post.dto.response;

public record PagePostResponse (

        Long postId,
        Long lastPostId,
        String title,
        String content,
        String author
) {
}
