package com.spring.common.fixture;

import com.spring.post.domain.Post;
import com.spring.user.domain.User;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostFixture {
    public static Post createPost(String title, User author) {
        return Post.of(
                title,
                "Test Context",
                author
        );
    }

}
