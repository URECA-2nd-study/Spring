package com.spring.post.controller;

import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.repository.PostRepository;
import com.spring.post.service.PostService;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PagingPostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PostService postService;

    @Test
    @DisplayName("게시글 목록을 정상적으로 조회할 수 있다")
    void getPostList() throws Exception {
        List<SimplePostResponse> postResponses = List.of(
                new SimplePostResponse(1L, "첫 번째 글", "내용1", "user1"),
                new SimplePostResponse(2L, "두 번째 글", "내용2", "user2"),
                new SimplePostResponse(3L, "세 번째 글", "내용3", "user3")
        );

        Slice<SimplePostResponse> postSlice = new SliceImpl<>(postResponses, PageRequest.of(0, 10), true);
        given(postService.getPostSlice(anyLong(), any(Pageable.class))).willReturn(postSlice);

        mockMvc.perform(get("/api/post")
                        .param("lastPostId", "5")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(postResponses.size()))
                .andExpect(jsonPath("$.content[0].title").value(postResponses.get(0).title()))
                .andExpect(jsonPath("$.content[1].title").value(postResponses.get(1).title()))
                .andDo(print());
    }
}
