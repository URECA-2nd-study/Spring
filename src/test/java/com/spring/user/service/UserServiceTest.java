package com.spring.user.service;

import com.spring.fixture.UserFixture;
import com.spring.post.dto.request.RegisterPostRequest;
import com.spring.post.repository.PostRepository;
import com.spring.post.service.PostService;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;

    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("[REQUIRED] 자식 예외서 터졌을 때")
    public void saveWithRequiredFailTest() {

        //given
        User user = UserFixture.createUser("aaa@naver.com","pw","황지연", Role.ADMIN);
        ReflectionTestUtils.setField(user,"id",20L);

        //when

        //then
        Assertions.assertThrows(RuntimeException.class, () -> userService.savewithRequiredFail(new RegisterUserRequest(user.getEmail(),user.getPassword(),user.getName())));
        //userService.savewithRequiredFail(new RegisterUserRequest(user.getEmail(),user.getPassword(),user.getName()));
        Assertions.assertEquals(0, userRepository.count());
        Assertions.assertEquals(0, postRepository.count());

    }

    @Test
    @DisplayName("[REQUIRES_NEW]  부모 메서드에서 예외 터졌을 때 자식은 독립적으로 실행 되는지 확인")
    public void saveWithRequiresNewFailTest() {

        //given
        User user = UserFixture.createUser("bbb@naver.com","pw","황지연2", Role.ADMIN);
//        ReflectionTestUtils.setField(user,"id",30L);

        //when

        //then
        Assertions.assertThrows(RuntimeException.class, () -> userService.savewithRequiresNewFail(new RegisterUserRequest(user.getEmail(),user.getPassword(),user.getName())));
        Assertions.assertEquals(0, userRepository.count());
        Assertions.assertEquals(1, postRepository.count());

    }


    @Test
    @DisplayName("[MANDATORY] 부모 트랜잭션 없이 B가 호출된다면 예외가 발생하는지 테스트")
    public void saveWithMandatoryFailTest() {

        //given
        User user = UserFixture.createUser("ccc@naver.com","pw","황지연3", Role.ADMIN);
        ReflectionTestUtils.setField(user,"id",30L);

        //when

        //then
        Assertions.assertThrows(IllegalTransactionStateException.class, () -> postService.savePostFailwithMandatory(new RegisterPostRequest("post mandatory","content",30L)));
        Assertions.assertEquals(0, postRepository.count());

    }



}
