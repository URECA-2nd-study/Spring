package com.spring.transaction;

import java.sql.SQLException;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.IllegalTransactionStateException;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import com.spring.user.service.UserService;
import com.spring.user.service.UserTransactionService;

@SpringBootTest
public class UserTransactionTest {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserTransactionService userTransactionService;

	@AfterEach
	void tearDown() {
		userRepository.deleteAllInBatch();
	}

	@DisplayName("트랜잭션 전파 수준이 required일 때 예외가 발생하면 같은 트랜잭션 내에 있는 모든 것이 롤백된다.")
	@Test
	void transactionalWithRequired() {
	    // when		// then
		Assertions.assertThatThrownBy(() -> userService.requiredA())
			.isInstanceOf(BaseException.class);
		// userService.requiredA();
		List<User> all = userRepository.findAll();
		Assertions.assertThat(all).hasSize(0);
	}

	@DisplayName("트랜잭션 전파 수준이 requiredNew일 때 상위 트랜잭션에서 예외가 발생해도 하위 트랜잭션은 커밋된다.")
	@Test
	void transactionalWithRequiredNew() {
	    // when		// then
		Assertions.assertThatThrownBy(() -> userService.requiredNewA())
			.isInstanceOf(BaseException.class);

		List<User> all = userRepository.findAll();
		Assertions.assertThat(all).hasSize(1);
	}

	@DisplayName("트랜잭션 전파 수준이 Mandatory일 때 상위 메서드에서 트랜잭션이 전파되지 않는다면 예외가 발생한다.")
	@Test
	void nonTransactionalWithMandatory() {
	    // when		// then
		Assertions.assertThatThrownBy(() -> userService.mandatoryAWithNonTransaction())
			.isInstanceOf(IllegalTransactionStateException.class);

		List<User> all = userRepository.findAll();
		Assertions.assertThat(all).hasSize(1);
	}

	@DisplayName("트랜잭션 전파 수준이 Mandatory일 때 상위 메서드에서 트랜잭션이 전파된다면 예외가 발생하지 않는다.")
	@Test
	void transactionalWithMandatory() {
	    // when
		userService.mandatoryAWithTransaction();

		// then
		List<User> all = userRepository.findAll();
		Assertions.assertThat(all).hasSize(2);
	}
}
