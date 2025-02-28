package com.spring.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.post.common.fixture.PostFixtures;
import com.spring.post.domain.Post;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.repository.PostRepository;
import com.spring.user.common.fixture.UserFixtures;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

//    @AfterEach
//    void tearDown() {
//        postRepository.deleteAllInBatch();
//        userRepository.deleteAllInBatch();
//    }

    @DisplayName("POST Update API 통합 테스트")
    @Test
    void updatePostAPITest() throws Exception {
        // given
        User user = userRepository.save(UserFixtures.createUser());
        ReflectionTestUtils.setField(user, "id", 1L);
        Post post = postRepository.save(PostFixtures.createPost(user));
        UpdatePostRequest req = new UpdatePostRequest(user.getId(), "수정 제목", "수정 내용");

        // when & then
        mockMvc.perform(patch("/api/post/{postId}", post.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(req)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.postId").value(post.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value(req.title()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(req.content()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(user.getName()));
    }
}
