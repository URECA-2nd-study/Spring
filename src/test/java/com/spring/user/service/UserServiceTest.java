package com.spring.user.service;

import com.spring.post.domain.Post;
import com.spring.post.repository.PostRepository;
import com.spring.user.common.fixture.UserFixtures;
import com.spring.user.domain.User;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @AfterEach
    void teardown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Required Test")
    void requiredTest() {
        // Given
        User user = UserFixtures.createUser();
        ReflectionTestUtils.setField(user, "id", 100L);

        //when
        try {
            userService.testRequiredA(new RegisterUserRequest(user.getEmail(), user.getPassword(), user.getName()));
        } catch (Exception e) {
            System.out.println("에러 발생");
        }

        // then
        List<User> userAll = userRepository.findAll();
        List<Post> postAll = postRepository.findAll();
        assertAll(
            () -> assertThat(userAll).hasSize(0),
            () -> assertThat(postAll).hasSize(0)
        );
    }

    @Test
    @DisplayName("Required Test")
    void requiresNewTest() {
        // Given
        User user = UserFixtures.createUser();
        ReflectionTestUtils.setField(user, "id", 100L);

        //when
        try {
            userService.testRequiresNewA(new RegisterUserRequest(user.getEmail(), user.getPassword(), user.getName()));
        } catch (Exception e) {
            System.out.println("에러 발생");
        }

        // then
        // 유저는 저장, Post는 미저장
        List<User> userAll = userRepository.findAll();
        List<Post> postAll = postRepository.findAll();
        assertAll(
            () -> assertThat(userAll).hasSize(1),
            () -> assertThat(postAll).hasSize(0)
        );
    }

    // mendatory : A는 @Transactional 안붙이기
    // B는 Mendatory -> user는 롤백안됨(Transaction아니니까), mendatory는 에러 발생

    /*
    * Transaction Isolation Test
    * */

//    @Test
//    @DisplayName("READ_UNCOMMITTED 테스트")
//    void changePointWithDirtyReadTest() {
//        User user = UserFixtures.createUserWithPoint();
//
//        CompletableFuture<Void> updatePoint = CompletableFuture.runAsync(() -> {
//            try {
//                userService.changePoint(user.getId(), BigDecimal.valueOf(100));
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        CompletableFuture<Void> printPoint = CompletableFuture.supplyAsync(() -> {
//            System.out.println(userService.findUserByEmail(user.getEmail()).getPoint());
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            System.out.println(userService.findUserByEmail(user.getEmail()).getPoint());
//            return null;
//        });
//
//        CompletableFuture.allOf(updatePoint, printPoint).join();
//    }

    @DisplayName("READ_UNCOMMITTED 테스트")
    @Test
    void readUnCommittedTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixtures.createUserWithPoint());

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userService.changePointReadUncommitted(savedUser.getId(), new BigDecimal("1000"));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();//다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[READ_UNCOMMITTED] findUser.point =  " + findUser.getPoint());
    }

    @DisplayName("READ_COMMITTED 테스트")
    @Test
    void readCommittedTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixtures.createUserWithPoint());

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userService.changePointReadCommitted(savedUser.getId(), new BigDecimal("1000"));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();//다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[READ_COMMITTED] findUser.point =  " + findUser.getPoint());
    }

    @DisplayName("REPEATABLE_READ 테스트")
    @Test
    void repeatableTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixtures.createUserWithPoint());

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userService.changePointRepeatableRead(savedUser.getId(), new BigDecimal("1000"));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();//다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[REPEATABLE_READ] findUser.point =  " + findUser.getPoint());
    }

    @DisplayName("SERIALIZABLE 테스트")
    @Test
    void serializableTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixtures.createUserWithPoint());

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userService.changePointSerializable(savedUser.getId(), new BigDecimal("1000"));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();//다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[SERIALIZABLE] findUser.point =  " + findUser.getPoint());
    }


}
