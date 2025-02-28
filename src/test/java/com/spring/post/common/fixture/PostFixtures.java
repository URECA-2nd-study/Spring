package com.spring.post.common.fixture;

import com.spring.post.domain.Post;
import com.spring.user.common.fixture.UserFixtures;
import com.spring.user.domain.User;

import java.util.ArrayList;
import java.util.List;

public class PostFixtures {
    // 포스트 객체 리스트 생성
    public static List<Post> createPosts() {
        List<Post> posts = new ArrayList<>();
        User user = UserFixtures.createUser();
        int count = 1;
        for (int i = 0; i < 20; i++) {
            posts.add(Post.of(i + "번째 게시물", "안녕하세요, " + i + "님", user));
        }
        return posts;
    }

    public static Post createPost(User user) {
        return Post.of("제목", "내용", user);
    }
}
