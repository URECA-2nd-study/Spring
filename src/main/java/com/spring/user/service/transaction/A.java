package com.spring.user.service.transaction;

import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class A {
    @Autowired UserService userService;
    @Autowired B b;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void a(RegisterUserRequest request1, RegisterUserRequest request2){
        userService.joinUser(request1);
        try {
            b.b(request2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
