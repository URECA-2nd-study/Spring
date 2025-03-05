package com.spring.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.IllegalTransactionStateException;

@SpringBootTest
public class TransactionTest {

    @Autowired
    private TransactionService1 transactionService1;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("REQUIRED 전파는 상위 메소드를 롤백시킨다")
    public void testTransactionRollback(){
        // given
        User initialUser = User.of("e1", "p1", "n1", Role.MEMBER);
        ReflectionTestUtils.setField(initialUser, "id", 1L);
        userRepository.save(initialUser);

        // when
        // 유저 값 업데이트 후 Unchecked 예외 발생
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService1.testRequiredA();
        });

        // then
        User rolledBackUser = userRepository.findById(initialUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertEquals(initialUser.getEmail(), rolledBackUser.getEmail()); // 이메일이 변경되지 않았어야 함
        assertEquals(initialUser.getName(), rolledBackUser.getName()); // 이름이 변경되지 않았어야 함
    }

    @Test
    @DisplayName("REQUIRES_NEW 전파는 상위 메소드를 롤백시키지 않는다")
    public void testRequiresNewRollback() {
        // given
        User initialUser = User.of("e1", "p1", "n1", Role.MEMBER);
        ReflectionTestUtils.setField(initialUser, "id", 1L);
        userRepository.save(initialUser);

        // when
        transactionService1.testRequiredNewA();

        // then
        User rolledBackUser = userRepository.findById(initialUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        assertEquals("e2", rolledBackUser.getEmail()); // 이메일이 변경되어야 함
        assertEquals("n2", rolledBackUser.getName()); // 이름이 변경되어야 함
    }

    @Test
    @DisplayName("MANDATORY 부모가 트랜잭션이 없을 때 예외 발생")
    public void testMandatoryPropagationException() {
        // 기본적으로 트랜잭션이 없는 상태에서 method A 호출
        Exception exception = assertThrows(RuntimeException.class, () -> {
            transactionService1.testMandatoryA();
        });

        // 예외 메시지 확인
        assertTrue(exception instanceof IllegalTransactionStateException);
    }

}
