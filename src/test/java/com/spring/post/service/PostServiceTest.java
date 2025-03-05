package com.spring.post.service;

import static org.junit.jupiter.api.Assertions.*;

import com.spring.fixture.UserFixture;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("정상적으로 게시물 업데이트")
    void updatePost(){
        // given
        UpdatePostRequest request = new UpdatePostRequest(1L, "제목", "내용 업데이트");
        User user = UserFixture.createUser("1@gmail.com", Role.MEMBER);
        ReflectionTestUtils.setField(user, "id", 1L);

        // when


        // then
    }
}