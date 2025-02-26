package com.spring.post.repository;

import com.spring.post.domain.Post;
import java.util.List;

public interface PostRepositoryCustom {
    boolean exists();
    void batchInsert(List<Post> posts);
}
