package com.spring.post.repository;

import com.spring.post.dto.response.PagePostResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {

    public Slice<PagePostResponse> searchPostPageBasic(Long postId, Pageable pageable);
}
