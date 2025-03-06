package com.spring.user.service;

import com.spring.common.fixture.UserFixture;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserTransactionServiceTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserTransactionService userTransactionService;

    @DisplayName("[트랜잭션 격리 레벨] READ_UNCOMMITTED")
    @Test
    void readUnCommittedTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixture.createUserByEmail("readUnCommited1@test.com"));

        int threadCount = 10_000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userTransactionService.readUnCommittedTransaction(savedUser.getId(), new BigDecimal("1000"));
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();//다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[READ_UNCOMMITTED] findUser.point = {} " + findUser.getPoint());
    }

    @DisplayName("[트랜잭션 격리 레벨] READ_COMMITTED")
    @Test
    void readCommittedTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixture.createUserByEmail("readUnCommited1@test.com"));

        int threadCount = 10_000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userTransactionService.readCommittedTransaction(savedUser.getId(), new BigDecimal("1000"));
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[READ_COMMITTED] findUser.point = {} " + findUser.getPoint());
    }

    @DisplayName("[트랜잭션 격리 레벨] REPEATABLE_READ")
    @Test
    void readRepeatableReadTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixture.createUserByEmail("readUnCommited1@test.com"));

        int threadCount = 10_000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userTransactionService.readRepeatableReadTransaction(savedUser.getId(), new BigDecimal("1000"));
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[REPEATABLE_READ] findUser.point = {} " + findUser.getPoint());
    }

    @DisplayName("[트랜잭션 격리 레벨] SERIALIZABLE")
    @Test
    void readSerializableTransaction() throws InterruptedException {
        // given
        User savedUser = userRepository.save(UserFixture.createUserByEmail("readUnCommited1@test.com"));

        int threadCount = 10_000;
        ExecutorService executorService = Executors.newFixedThreadPool(16);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    userTransactionService.readSerializableTransaction(savedUser.getId(), new BigDecimal("1000"));
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        User findUser = userRepository.findById(savedUser.getId()).get();
        // then
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("1000"));
        System.out.println("[SERIALIZABLE] findUser.point = {} " + findUser.getPoint());
    }

}