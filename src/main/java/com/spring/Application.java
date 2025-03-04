package com.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


}


/*
전파 옵션 REQUIRED
서비스 테스트 클래스를 만들어 A메서드와 B메서드를 만들고 B를 호출하여 A에서 익셉션을 주어 롤백이 되는 지 확인하는 실습
 */

