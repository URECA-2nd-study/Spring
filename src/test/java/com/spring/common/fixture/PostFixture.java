package com.spring.common.fixture;

import com.spring.post.domain.Post;
import com.spring.user.domain.User;

public class PostFixture {

    public static Post createPost(String title, User user) {
        return Post.of(title, "안녕하세요", user);
    }
}
