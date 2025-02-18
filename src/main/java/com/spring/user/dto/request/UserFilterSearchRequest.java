package com.spring.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFilterSearchRequest {
    private String email;
    private String name;
    private String role;

}
