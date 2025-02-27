package com.spring.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.common.fixture.PostFixture;
import com.spring.common.fixture.UserFixture;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private PostRepository postRepository;
    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("포스트를 정상적으로 업데이트할 수 있다")
    public void updatePostTest() throws Exception {
        User user = userRepository.save(UserFixture.createUser("test@abc.dom",Role.ADMIN));
        Post post = postRepository.save(PostFixture.createPost("test title",user));

        UpdatePostRequest updatePostRequest = new UpdatePostRequest(user.getId(),"updated title", "updated content");

        mockMvc.perform(patch("/api/post/{postId}", post.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(updatePostRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(post.getId()))
                .andExpect(jsonPath("$.title").value(updatePostRequest.title()))
                .andExpect(jsonPath("$.content").value(updatePostRequest.content()))
                .andExpect(jsonPath("$.author").value(user.getName()))
                .andDo(print());
    }

    @Test
    @DisplayName("포스트 주인이 아닌 유저는 포스트를 업데이트할 수 없다")
    public void notUpdatePostTest() throws Exception {
        User user = userRepository.save(UserFixture.createUser("test@abc.dom",Role.ADMIN));
        Post post = postRepository.save(PostFixture.createPost("test title",user));

        User order = userRepository.save(UserFixture.createUser("order@abc.dom",Role.ADMIN));

        UpdatePostRequest updatePostRequest = new UpdatePostRequest(order.getId(),"updated title", "updated content");

        mockMvc.perform(patch("/api/post/{postId}", post.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatePostRequest)))
                .andExpect(status().isBadRequest());
    }

}
