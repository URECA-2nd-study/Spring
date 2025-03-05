package com.spring.user.service;

import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService1 {

    private final UserRepository userRepository;
    private final TransactionService2 transactionService2;

    @Transactional  // REQUIRED 전파 옵션
    public void testRequiredA() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.updateInfo("e2", "n2", "어드민"); // 값 업데이트
        transactionService2.testRequiredB(); // 예외 발생
    }

    @Transactional // REQUIRED 전파 옵션
    public void testRequiredNewA() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.updateInfo("e2", "n2", "어드민"); // 값 업데이트

        try {
            transactionService2.testRequiredNewB(); // REQUIRES_NEW로 예외 호출
        } catch (Exception e) {
            log.error("error in testRequiredNewB");
        }
    }

    public void testMandatoryA() {
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.updateInfo("e2", "n2", "어드민"); // 값 업데이트
        transactionService2.testMandatorydB(); // 예외 발생
    }
}
