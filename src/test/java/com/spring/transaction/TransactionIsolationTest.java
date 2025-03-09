package com.spring.transaction;

import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.spring.common.fixture.UserFixture;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import com.spring.user.service.UserTransactionService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class TransactionIsolationTest {

	@Autowired
	UserTransactionService userTransactionService;

	@Autowired
	UserRepository userRepository;

	static final int THREAD_COUNT = 100;

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@DisplayName("[트랜잭션 격리 레벨] READ_UNCOMMITTED")
	@Test
	void readUnCommittedTransaction() throws InterruptedException {
	    // given
		User savedUser = userRepository.save(UserFixture.user("readUnCommited1@test.com"));

		ExecutorService executorService = Executors.newFixedThreadPool(32);

		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		// when
		for (int i = 0; i < THREAD_COUNT; i++) {
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
		log.info("[READ_UNCOMMITTED] findUser.point = {} ", findUser.getPoint());
	}

	@DisplayName("[트랜잭션 격리 레벨] READ_COMMITTED")
	@Test
	void readCommittedTransaction() throws InterruptedException {
	    // given
		User savedUser = userRepository.save(UserFixture.user("readUnCommited2@test.com"));

		ExecutorService executorService = Executors.newFixedThreadPool(32);

		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		// when
		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					userTransactionService.readCommittedTransaction(savedUser.getId(), new BigDecimal("1000"));
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
		log.info("[READ_COMMITTED] findUser.point = {} ", findUser.getPoint());
	}

	@DisplayName("[트랜잭션 격리 레벨] REPEATABLE_READ")
	@Test
	void repeatableReadTransaction() throws InterruptedException {
	    // given
		User savedUser = userRepository.save(UserFixture.user("readUnCommited3@test.com"));

		ExecutorService executorService = Executors.newFixedThreadPool(32);

		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		// when
		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					userTransactionService.repeatableReadTransaction(savedUser.getId(), new BigDecimal("1000"));
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
		log.info("[REPEATABLE_READ] findUser.point = {} ", findUser.getPoint());
	}

	@DisplayName("[트랜잭션 격리 레벨] SERIALIZABLE")
	@Test
	void serializableTransaction() throws InterruptedException {
	    // given
		User savedUser = userRepository.save(UserFixture.user("readUnCommited4@test.com"));

		ExecutorService executorService = Executors.newFixedThreadPool(32);

		CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

		// when
		for (int i = 0; i < THREAD_COUNT; i++) {
			executorService.submit(() -> {
				try {
					userTransactionService.serializableTransaction(savedUser.getId(), new BigDecimal("1000"));
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
		log.info("[SERIALIZABLE] findUser.point = {} ", findUser.getPoint());
	}

}
