package com.spring.user.repository;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCustomRepository {

    public List<User> findAllUsersByRole(Role role);
}
