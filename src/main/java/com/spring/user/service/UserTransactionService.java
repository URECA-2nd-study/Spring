package com.spring.user.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTransactionService {

	private final UserRepository userRepository;

	@Transactional
	public void requiredB() {
		userRepository.save(User.of("required2@naver.com", "1234", "김트랜2", Role.MEMBER));
		throw new BaseException(UserErrorCode.DUPLICATED_EMAIL);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void requiredNewB() {
		User savedUser = userRepository.save(User.of("required4@naver.com", "1234", "김트랜4", Role.MEMBER));

		List<User> userAll = userRepository.findAll();
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void mandatoryBNonTransaction() {
		User savedUser = userRepository.save(User.of("required6@naver.com", "1234", "김트랜6", Role.MEMBER));
		log.info("mandatoryBNonTransaction() 호출!!");
	}

	@Transactional(propagation = Propagation.MANDATORY)
	public void mandatoryBTransaction() {
		User savedUser = userRepository.save(User.of("required8@naver.com", "1234", "김트랜8", Role.MEMBER));
		log.info("mandatoryBTransaction() 호출!!");
	}

	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void readUnCommittedTransaction(Long userId, BigDecimal point) {
		User findUser = userRepository.findById(userId).orElseThrow(
			() -> new BaseException(UserErrorCode.NOT_FOUND_USER)
		);

		findUser.updatePoint(point);
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	public void readCommittedTransaction(Long userId, BigDecimal point) {
		User findUser = userRepository.findById(userId).orElseThrow(
			() -> new BaseException(UserErrorCode.NOT_FOUND_USER)
		);

		findUser.updatePoint(point);
	}
	@Transactional(isolation = Isolation.REPEATABLE_READ)
	public void repeatableReadTransaction(Long userId, BigDecimal point) {
		User findUser = userRepository.findById(userId).orElseThrow(
			() -> new BaseException(UserErrorCode.NOT_FOUND_USER)
		);

		findUser.updatePoint(point);
	}
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void serializableTransaction(Long userId, BigDecimal point) {
		User findUser = userRepository.findById(userId).orElseThrow(
			() -> new BaseException(UserErrorCode.NOT_FOUND_USER)
		);

		findUser.updatePoint(point);
	}
}
