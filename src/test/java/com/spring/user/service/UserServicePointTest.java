package com.spring.user.service;

import com.spring.common.fixture.UserFixture;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import com.spring.user.service.transactionisolation.UserServiceIsolation;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@SpringBootTest
public class UserServicePointTest {

    @Autowired private UserServiceIsolation userServiceP;
    @Autowired private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("Read Uncommitted 격리 수준 테스트")
    public void readUncommitedTest() throws InterruptedException {
        TemplateIsolation templateIsolation = new TemplateIsolation(userRepository) {
            @Override
            public void call(Long userId, BigDecimal point) {
                userServiceP.readUncomittedPlusPoint(userId, point);
            }
        };
        User user = userRepository.save(UserFixture.createUser("123", Role.ADMIN, BigDecimal.ZERO));
        User findUser = templateIsolation.excute(user);
        System.out.println("point= " + findUser.getPoint());
    }
    @Test
    @DisplayName("READ COMMITTED 격리 수준 테스트")
    public void readCommitedTest() throws InterruptedException {
        TemplateIsolation templateIsolation = new TemplateIsolation(userRepository) {
            @Override
            public void call(Long userId, BigDecimal point) {
                userServiceP.readComittedPlusPoint(userId, point);
            }
        };
        User user = userRepository.save(UserFixture.createUser("123", Role.ADMIN, BigDecimal.ZERO));
        User findUser = templateIsolation.excute(user);
        System.out.println("point= " + findUser.getPoint());
    }

    @Test
    @DisplayName("REPEATABLE READ 격리 수준 테스트")
    public void repeatableReadTest() throws InterruptedException {
        TemplateIsolation templateIsolation = new TemplateIsolation(userRepository) {
            @Override
            public void call(Long userId, BigDecimal point) {
                userServiceP.repeatableReadPlus(userId, point);
            }
        };
        User user = userRepository.save(UserFixture.createUser("123", Role.ADMIN, BigDecimal.ZERO));
        User findUser = templateIsolation.excute(user);
        System.out.println("point= " + findUser.getPoint());
    }

    @Test
    @DisplayName("SERIALIZABLE 격리 수준 테스트")
    public void serializableTest() throws InterruptedException {
        TemplateIsolation templateIsolation = new TemplateIsolation(userRepository) {
            @Override
            public void call(Long userId, BigDecimal point) {
                userServiceP.serializablePlus(userId, point);
            }
        };
        User user = userRepository.save(UserFixture.createUser("123", Role.ADMIN, BigDecimal.ZERO));
        User findUser = templateIsolation.excute(user);
        System.out.println("point= " + findUser.getPoint());
    }
}

@RequiredArgsConstructor
abstract class TemplateIsolation{
    private final UserRepository userRepository;

    public User excute(User user) throws InterruptedException {
        User savedUser = userRepository.save(user);

        int threadCount = 10000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try{
                    call(user.getId(), BigDecimal.valueOf(1000));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        User findUser = userRepository.findById(savedUser.getId()).get();
        return findUser;
    }
    public abstract void call(Long userId, BigDecimal point);
}