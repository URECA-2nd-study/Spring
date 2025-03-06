package com.spring.user.service;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceB {

    private final UserRepository userRepository;

    @Transactional
    public void requiredB() throws BaseException {
        userRepository.save(User.of("abcd@gmail.com", "asdf", "신이름", Role.MEMBER));
        throw new BaseException(UserErrorCode.INVALID_ROLE);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewB() throws BaseException {
        userRepository.save(User.of("abcd@gmail.com", "asdf", "신이름", Role.MEMBER));
        throw new BaseException(UserErrorCode.INVALID_ROLE);
    }
}
