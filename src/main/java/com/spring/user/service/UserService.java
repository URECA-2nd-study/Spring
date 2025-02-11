package com.spring.user.service;

import com.spring.user.domain.User;
import com.spring.user.dto.GetUserListRes;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<GetUserListRes> getUserList() {
        return userRepository.findAll().stream().map(GetUserListRes::new).toList();
    }
}
