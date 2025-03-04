package com.spring.post.repository;

import com.spring.post.dto.response.PagePostResponse;
import org.springframework.data.domain.Pageable;

public interface PostCustomRepository {

    public PagePostResponse searchPostByPagination(Long postId, Pageable pageable);
}
