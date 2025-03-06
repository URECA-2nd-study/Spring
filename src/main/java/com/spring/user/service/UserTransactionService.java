package com.spring.user.service;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.domain.User;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserTransactionService {

    private final UserRepository userRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void readUnCommittedTransaction(Long id, BigDecimal point) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BaseException(UserErrorCode.NOT_FOUND_USER));

        user.updatePoint(point);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void readCommittedTransaction(Long id, BigDecimal point) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BaseException(UserErrorCode.NOT_FOUND_USER));

        user.updatePoint(point);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void readRepeatableReadTransaction(Long id, BigDecimal point) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BaseException(UserErrorCode.NOT_FOUND_USER));

        user.updatePoint(point);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void readSerializableTransaction(Long id, BigDecimal point) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new BaseException(UserErrorCode.NOT_FOUND_USER));

        user.updatePoint(point);
    }
}
