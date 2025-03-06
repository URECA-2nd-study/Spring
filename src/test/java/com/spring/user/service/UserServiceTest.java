package com.spring.user.service;

import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.SimpleUserRequest;
import com.spring.user.service.transactionpropagation.UserServiceA;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserServiceA userServiceA;

    @Test
    @DisplayName("REQUIRED는 B에서 롤백되면 A도 롤백된다")
    public void rollbackTest1(){
        RegisterUserRequest request1 = new RegisterUserRequest("test1@gmail.com","test1","test1");
        RegisterUserRequest request2 = new RegisterUserRequest("test2@gmail.com","test2","test2");
        try {
            userServiceA.requiredA(request1, request2); // request1은 methodA에서 저장, request2는 methodB에서 저장
        }
        catch (Exception e){
            assertThat(userService.getUserAll().size()).isEqualTo(0);// 모두 롤백되어 size는 0
        }
    }

    @Test
    @DisplayName("REQUIRED_NEW는 B에서 롤백되도 A에서 커밋될 수 있다")
    public void rollbackTest2(){
        RegisterUserRequest request1 = new RegisterUserRequest("test1@gmail.com","test1","test1");
        RegisterUserRequest request2 = new RegisterUserRequest("test2@gmail.com","test2","test2");
        userServiceA.requiresNewA(request1, request2); // request1은 methodA에서 저장, request2는 methodB에서 저장
        assertThat(userService.getUserAll().size()).isEqualTo(1L); // A만 커밋되어 size는 1
        assertThat(userService.getUser(new SimpleUserRequest(1L)).email()).isEqualTo("test1@gmail.com");
    }

}