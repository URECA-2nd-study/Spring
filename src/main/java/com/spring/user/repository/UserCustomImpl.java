package com.spring.user.repository;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.user.domain.QUser;
import com.spring.user.domain.Role;
import com.spring.user.dto.response.QSimpleUserResponse;
import com.spring.user.dto.response.SimpleUserResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCustomImpl implements UserCustom {

	private final JPAQueryFactory queryFactory;

	@Override
	public List<SimpleUserResponse> getFilteredUsers(String role) {
		QUser u = QUser.user;

		return queryFactory
			.select(new QSimpleUserResponse(u))
			.from(u)
			.where(userRoleContains(role))
			.fetch();
	}

	private BooleanExpression userRoleContains(String role) {
		if(Objects.isNull(role) || role.isBlank()) {
			return null;
		}
		Role roleObj = Role.of(role);
		return QUser.user.role.in(roleObj);
	}
}
