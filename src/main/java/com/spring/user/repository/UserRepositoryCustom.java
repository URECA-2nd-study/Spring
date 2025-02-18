package com.spring.user.repository;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.util.List;

public interface UserRepositoryCustom {
    public List<User> findAllByRole(Role role);
}
