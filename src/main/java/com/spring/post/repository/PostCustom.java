package com.spring.post.repository;

import java.util.List;


import com.spring.post.dto.response.NoOffsetPostResponse;

public interface PostCustom {

	NoOffsetPostResponse paginationPostWithNoOffset(Long lastPostId);
}
