package com.spring.post.repository;

import com.spring.post.domain.Post;
import com.spring.post.dto.response.SimplePostResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom{
    Slice<SimplePostResponse> searchPostPages(Long lastPostId, Pageable pageable);
}
