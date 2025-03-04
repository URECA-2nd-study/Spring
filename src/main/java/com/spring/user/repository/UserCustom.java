package com.spring.user.repository;

import java.util.List;

import com.spring.user.dto.response.SimpleUserResponse;

public interface UserCustom {

	List<SimpleUserResponse> getFilteredUsers(String role);
}
