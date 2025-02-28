package com.spring.post.repository;

import com.spring.post.domain.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomPostRepository {
    List<Post> findAllByPageable(Pageable pageable, Long lastPostId);
}
