package com.spring.user.service;

import com.spring.common.fixture.UserFixture;
import com.spring.post.dto.request.RegisterPostRequest;
import com.spring.post.dto.request.SimplePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.SimpleUserRequest;
import com.spring.user.repository.UserRepository;
import com.spring.user.service.transaction.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired private UserRepository userRepository;
    @Autowired private UserService userService;
    @Autowired private A a;

    @Test
    public void rollbackTest1(){
        try {
            RegisterUserRequest request1 = new RegisterUserRequest("test1@gmail.com","test1","test1");
            RegisterUserRequest request2 = new RegisterUserRequest("test2@gmail.com","test2","test2");
            a.a(request1, request2);
            assertThat(userRepository.findAll().size()).isEqualTo(2);
            assertThat(userRepository.findById(1L).get().getEmail()).isEqualTo("test1@gmail.com");
            assertThat(userRepository.findById(2L).get().getEmail()).isEqualTo("test2@gmail.com");
        }catch (Exception e){
            e.printStackTrace();
            assertThat(userRepository.findAll().size()).isEqualTo(0);
        }
    }

    @Test
    public void rollbackTest2(){
        try {
            RegisterUserRequest request1 = new RegisterUserRequest("test1@gmail.com","test1","test1");
            RegisterUserRequest request2 = new RegisterUserRequest("test2@gmail.com","test2","test2");
            a.a(request1, request2);
            assertThat(userRepository.findAll().size()).isEqualTo(1);
            assertThat(userRepository.findById(1L).get().getEmail()).isEqualTo("test1@gmail.com");
        }catch (Exception e){
            e.printStackTrace();
            assertThat(userRepository.findAll().size()).isEqualTo(0);
        }
    }

}