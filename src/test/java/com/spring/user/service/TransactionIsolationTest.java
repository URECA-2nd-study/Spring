package com.spring.user.service;

import com.spring.fixture.UserFixture;
import com.spring.post.repository.PostRepository;
import com.spring.post.service.PostService;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@SpringBootTest
public class TransactionIsolationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserService userService;


    @AfterEach
    void tearDown() {
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("[ISOLATION] Transaction READ_UNCOMMITED")
    public void isolationWithUncommitedTest() throws InterruptedException {

        //given
        User user = UserFixture.createUser("read_uncommited@gmail.com","pwd","JIYEON", Role.MEMBER, new BigDecimal("1000"));
        User savedUser = userRepository.save(user);


        int numThreads = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(33);

        CountDownLatch latch = new CountDownLatch(numThreads);

        //when
        for(int i = 0; i<numThreads; i++){
            executor.submit(()->{
                try{
                    userService.changePointWithReadUncommitted(savedUser.getId(), new BigDecimal("1000"));
                }
                finally {
                    latch.countDown();
                }

            });
        }

        latch.await();

        User findUser = userRepository.findById(savedUser.getId()).get();

        //then
        Assertions.assertTrue(findUser.getPoint().compareTo(new BigDecimal("1000")) > 0);
        System.out.println(	"\n\n [READ_UNCOMMITTED] findUser.point = {} "+ findUser.getPoint() + "\n\n");
    }

    @Test
    @DisplayName("[ISOLATION] Transaction READ_COMMITED")
    public void isolationWithCommitedTest() throws InterruptedException {

        //given
        User user = UserFixture.createUser("read_commited@gmail.com","pwd","JIYEON", Role.MEMBER, new BigDecimal("1000"));
        User savedUser = userRepository.save(user);


        int numThreads = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(33);

        CountDownLatch latch = new CountDownLatch(numThreads);

        //when
        for(int i = 0; i<numThreads; i++){
            executor.submit(()->{
                try{
                    userService.changePointwithReadCommitted(savedUser.getId(), new BigDecimal("1000"));
                }
                finally {
                    latch.countDown();
                }

            });
        }

        latch.await();

        User findUser = userRepository.findById(savedUser.getId()).get();

        //then
        Assertions.assertTrue(findUser.getPoint().compareTo(new BigDecimal("1000")) > 0);
        System.out.println(	"\n\n [READ_COMMITTED] findUser.point = {} "+ findUser.getPoint() + "\n\n");
    }

    @Test
    @DisplayName("[ISOLATION] Transaction REPEATABLE_READ")
    public void isolationWithRepeatableReadTest() throws InterruptedException {

        //given
        User user = UserFixture.createUser("repeatableRead@gmail.com","pwd","JIYEON", Role.MEMBER, new BigDecimal("1000"));
        User savedUser = userRepository.save(user);


        int numThreads = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(33);

        CountDownLatch latch = new CountDownLatch(numThreads);

        //when
        for(int i = 0; i<numThreads; i++){
            executor.submit(()->{
                try{
                    userService.changePointwithRepeatableRead(savedUser.getId(), new BigDecimal("1000"));
                }
                finally {
                    latch.countDown();
                }

            });
        }

        latch.await();

        User findUser = userRepository.findById(savedUser.getId()).get();

        //then
        Assertions.assertTrue(findUser.getPoint().compareTo(new BigDecimal("1000")) > 0);
        System.out.println(	"\n\n [REPEATABLE_READ] findUser.point = {} "+ findUser.getPoint() + "\n\n");
    }

    @Test
    @DisplayName("[ISOLATION] Transaction SERIALIZABLE")
    public void isolationWithSerializableTest() throws InterruptedException {

        //given
        User user = UserFixture.createUser("serializable@gmail.com","pwd","JIYEON", Role.MEMBER, new BigDecimal("1000"));
        User savedUser = userRepository.save(user);


        int numThreads = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(33);

        CountDownLatch latch = new CountDownLatch(numThreads);

        //when
        for(int i = 0; i<numThreads; i++){
            executor.submit(()->{
                try{
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1, 10)); //각 스레드 마다 1~10ms 랜덤 sleep
                    userService.changePointwithSerializable(savedUser.getId(), new BigDecimal("1000"));
                }catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                finally {
                    latch.countDown();
                }

            });
        }

        latch.await();

        User findUser = userRepository.findById(savedUser.getId()).get();

        //then
        Assertions.assertTrue(findUser.getPoint().compareTo(new BigDecimal("1000")) > 0);
        System.out.println(	"\n\n [SERIALIZABLE] findUser.point = {} "+ findUser.getPoint() + "\n\n");
    }




}
