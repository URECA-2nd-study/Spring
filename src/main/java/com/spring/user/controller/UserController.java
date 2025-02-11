package com.spring.user.controller;

import com.spring.user.domain.User;
import com.spring.user.dto.GetUserListRes;
import com.spring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping()
    public List<GetUserListRes> getUserList() {
        return userService.getUserList();
    }

}
