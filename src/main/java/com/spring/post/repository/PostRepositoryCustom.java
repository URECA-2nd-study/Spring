package com.spring.post.repository;

import com.spring.post.domain.Post;
import com.spring.post.dto.request.SimplePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepositoryCustom{
    Slice<SimplePostResponse> searchPageSimple(SimplePostRequest condition, Pageable pageable);
}
