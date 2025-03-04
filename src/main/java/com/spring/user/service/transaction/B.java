package com.spring.user.service.transaction;

import com.spring.user.dto.request.RegisterUserRequest;
import com.spring.user.dto.request.SimpleUserRequest;
import com.spring.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class B {
    @Autowired private UserService userService;


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void b(RegisterUserRequest request2){
        try {
            userService.joinUser(request2);
            SimpleUserRequest request = null;
            userService.getUser(request);
        } catch (NullPointerException e) {
            throw new NullPointerException();
        }
    }

}
