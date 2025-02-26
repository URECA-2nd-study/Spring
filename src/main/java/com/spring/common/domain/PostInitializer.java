package com.spring.common.domain;

import com.spring.post.domain.Post;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class PostInitializer implements ApplicationRunner {

    private static final int POST_CNT = 10;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostInitializer(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!postRepository.exists()) {
            postRepository.batchInsert(createPosts());
        }
    }

    private List<Post> createPosts() {
        List<Post> posts = new ArrayList<>();
        List<User> users = userRepository.findAll(); // 모든 사용자 조회
        Random random = new Random();

        for (int i = 1; i <= POST_CNT; i++) {
            // 랜덤한 사용자 선택
            User randomUser = users.get(random.nextInt(users.size()));
            posts.add(createPost(randomUser));
        }
        return posts;
    }

    private Post createPost (User user) {
        return Post.of("title", "content", user);
    }
}
