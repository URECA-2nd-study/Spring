package com.spring.user.repository;

import com.spring.user.domain.User;
import com.spring.user.dto.request.UserFilterSearchRequest;

import java.util.List;
public interface UserCustomRepository {

    List<User> searchUsers(UserFilterSearchRequest request);

}
