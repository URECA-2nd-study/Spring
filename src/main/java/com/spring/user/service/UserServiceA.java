package com.spring.user.service;

import com.spring.common.exception.runtime.BaseException;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceA {

    private final UserRepository userRepository;
    private final UserServiceB userServiceB;

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredA() throws BaseException {
        userRepository.save(User.of("abc@gmail.com", "asdf", "김이름", Role.MEMBER));
        try {
            userServiceB.requiredB();
        } catch (BaseException e) {
            System.out.println("requiresB에서 예외 발생: " + e.getMessage());
        }
     }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewA() throws BaseException {
        userRepository.save(User.of("abc@gmail.com", "asdf", "김이름", Role.MEMBER));

        try {
            userServiceB.requiresNewB();
        } catch (BaseException e) {
            // B에서 발생한 예외를 잡아 A가 영향을 받지 않도록 한다.
            System.out.println("requiresNewB에서 예외 발생: " + e.getMessage());
        }
    }
}
