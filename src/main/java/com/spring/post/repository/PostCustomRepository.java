package com.spring.post.repository;

import com.spring.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostCustomRepository {

    Slice<Post> findAllbyId(Long cursorId, Pageable pageable);

}
