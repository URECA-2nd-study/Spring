package com.spring.user.service.transactionpropagation;

import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceB {
    private final UserService userService;

    @Autowired
    public UserServiceB(UserService userService) {
        this.userService = userService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void requiredB(RegisterUserRequest request){
        methodB(request);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void requiresNewB(RegisterUserRequest request){
        methodB(request);
    }

    private void methodB(RegisterUserRequest request){
        try {
            userService.joinUser(request);
            userService.getUser(null);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }

}
