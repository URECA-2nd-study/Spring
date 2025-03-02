package com.spring.post.service;
import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostServiceTest {

    @Autowired private PostService postService;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;

    @Test
    @DisplayName("포스트를 정상적으로 업데이트할 수 있다.")
    public void updatePostTest(){
        User user = userRepository.save(UserFixture.createUser("abc@abc.com", Role.ADMIN));
        Post post = postRepository.save(PostFixture.createPost("test title", user));

        UpdatePostRequest updatePostRequest = new UpdatePostRequest(user.getId(),
                "update test title",
                "update content");

        SimplePostResponse response = postService.updatePost(post.getId(), updatePostRequest);

        assertThat(response.title()).isEqualTo(updatePostRequest.title());
        assertThat(response.content()).isEqualTo(updatePostRequest.content());
        assertThat(response.author()).isEqualTo(user.getName());
    }


}
