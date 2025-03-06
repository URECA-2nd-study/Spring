package com.spring.user.service.transactionisolation;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.domain.User;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceIsolation {

    private final UserRepository userRepository;


    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void readComittedPlusPoint(Long userId, BigDecimal point) {
        plusPoint(userId, point);
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public void readUncomittedPlusPoint(Long userId, BigDecimal point) {
        plusPoint(userId, point);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void repeatableReadPlus(Long userId, BigDecimal point) {
        plusPoint(userId, point);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void serializablePlus(Long userId, BigDecimal point) {
        plusPoint(userId, point);
    }

    private void plusPoint(Long userId, BigDecimal point) {
        User user = findUser(userId);
        BigDecimal nextPoint = user.getPoint().add(point);
        user.updateInfo(user.getEmail(), user.getPassword(), nextPoint);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BaseException(UserErrorCode.NOT_FOUND_USER)
        );
    }

}
