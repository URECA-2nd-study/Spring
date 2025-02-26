package com.spring.post.repository;

import com.spring.post.domain.Post;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface PostRepositoryCustom {
    Slice<Post> findAllByPage(Long lastId, Pageable pageable);
    boolean exists();
    void batchInsert(List<Post> posts);
}
