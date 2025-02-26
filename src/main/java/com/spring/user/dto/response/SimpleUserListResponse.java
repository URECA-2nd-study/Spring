package com.spring.user.dto.response;

import java.util.List;

public record SimpleUserListResponse (
    List<SimpleUserResponse> userList
){
}
