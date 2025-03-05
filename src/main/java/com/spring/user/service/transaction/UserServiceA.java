package com.spring.user.service.transaction;

import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceA {
    private final UserService userService;
    private final UserServiceB userServiceB;

    @Autowired
    public UserServiceA(UserService userService, UserServiceB b) {
        this.userService = userService;
        this.userServiceB = b;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredA(RegisterUserRequest request1, RegisterUserRequest request2){
        methodA(request1, request2, "required");
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewA(RegisterUserRequest request1, RegisterUserRequest request2){
        methodA(request1, request2, "requiresNew");
    }

    private void methodA(RegisterUserRequest request1, RegisterUserRequest request2, String type){
        userService.joinUser(request1);
        try {
            if(type.equals("required")) userServiceB.requiredB(request2);
            else if(type.equals("requiresNew")) userServiceB.requiresNewB(request2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
