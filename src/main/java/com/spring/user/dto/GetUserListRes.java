package com.spring.user.dto;

import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import lombok.Getter;

@Getter
public class GetUserListRes {
    private long id;
    private String name;
    private String email;
    private Role role;


    public GetUserListRes(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
