package com.spring.common.fixture;

import com.spring.post.domain.Post;
import com.spring.user.domain.User;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PostFixture {

	public static Post post(String title, User author) {
		return Post.of(
			title,
			"테스트 작성 글 입니다.",
			author
		);
	}
}
