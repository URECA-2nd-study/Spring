package com.spring;

import com.spring.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationTests {

	@Test
	void checkEmail() {
		User user = new User("abc@naver.com", "ABC");
		Assertions.assertThat(user.getEmail()).isEqualTo("abc@naver.com");
	}
	@Test
	void checkName() {
		User user = new User("abc@naver.com", "ABC");
		Assertions.assertThat(user.getName()).isEqualTo("abc");
	}

}
