package com.spring.user.service;

import com.spring.fixture.UserFixture;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TransactionIsolTest {

    @Autowired
    private TransactionService1 transactionService1;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Read Uncommitted 격리 수준 테스트")
    void readUnCommittedTransaction() throws InterruptedException {
        // given
        User user = UserFixture.createUser("test@email.com", Role.MEMBER, BigDecimal.ZERO);
        userRepository.save(user);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);

        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    transactionService1.addPointReadUncommitted(user.getId(),
                            new BigDecimal("100")); // DB에 반영되지 않은 값을 읽어 업데이트
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();//다른 쓰레드에서 수행중인 작업이 완료될때까지 기다려줌

        // then
        // dirty read 발생
        User findUser = userRepository.findById(user.getId()).get();
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("100"));
        log.info("[READ_UNCOMMITTED] findUser.point = {} ", findUser.getPoint());
    }

    @Test
    @DisplayName("Repeatable Read 격리 수준 테스트")
    void repeatableReadTransaction() throws InterruptedException {
        // given
        User user = UserFixture.createUser("test@email.com", Role.MEMBER, BigDecimal.ZERO);

        userRepository.save(user);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    transactionService1.addPointRepeatableRead(user.getId(), new BigDecimal("100"));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 다른 스레드에서 수행중인 작업이 완료될 때까지 기다림

        // then
        User findUser = userRepository.findById(user.getId()).get();
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("100"));
        log.info("[REPEATABLE_READ] findUser.point = {} ", findUser.getPoint());
    }

    @Test
    @DisplayName("Serializable 격리 수준 테스트")
    void serializableTransaction() throws InterruptedException {
        // given
        User user = UserFixture.createUser("test@email.com", Role.MEMBER, BigDecimal.ZERO);

        userRepository.save(user);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    transactionService1.addPointSerializable(user.getId(), new BigDecimal("100"));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 다른 스레드에서 수행중인 작업이 완료될 때까지 기다림

        // then
        User findUser = userRepository.findById(user.getId()).get();
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("100"));
        log.info("[SERIALIZABLE] findUser.point = {} ", findUser.getPoint());
    }

    @Test
    @DisplayName("Read Committed 격리 수준 테스트")
    void readCommitted() throws InterruptedException {
        // given
        User user = UserFixture.createUser("test@email.com", Role.MEMBER, BigDecimal.ZERO);

        userRepository.save(user);

        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    transactionService1.addPointReadCommitted(user.getId(), new BigDecimal("100"));
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 다른 스레드에서 수행중인 작업이 완료될 때까지 기다림

        // then
        User findUser = userRepository.findById(user.getId()).get();
        Assertions.assertThat(findUser.getPoint()).isGreaterThan(new BigDecimal("100"));
        log.info("[SERIALIZABLE] findUser.point = {} ", findUser.getPoint());
    }
}
