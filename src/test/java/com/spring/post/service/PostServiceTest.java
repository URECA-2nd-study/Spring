package com.spring.post.service;


import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("post update가 잘 되는 지 확인")
    void updatePostWell(){

        //Given (데이터가 주어질 때)
        Long postId = 3L;
        User user = User.of("aaa@naver","root","기존유저", Role.ADMIN);
        ReflectionTestUtils.setField(user,"id",3L);
        Post post = Post.of("기존 Post","기존 content",user); //기존 Post
        UpdatePostRequest updatePost = new UpdatePostRequest(3L,"업데이트 Post","업데이트 content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        //When
        postService.updatePost(postId,updatePost);

        //then
        Assertions.assertEquals("업데이트 Post",post.getTitle());
        Assertions.assertEquals("업데이트 content",post.getContent());


    }
}
