package com.spring;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
/*
postService.updatePost() -일반 단위테스트(선택)
postService.updatePost() -with Mockito 단위테스트(필수)
postController.updatePost() - 일반 통합테스트(필수)
postService.updatePost() -with Mockito 통합테스트(필수)

 */