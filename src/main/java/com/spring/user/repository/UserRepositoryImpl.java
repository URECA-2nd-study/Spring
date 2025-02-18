package com.spring.user.repository;

import static com.spring.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findAllByRole(Role role) {
        List<User> users = queryFactory
                .selectFrom(user)
                .where(eqRole(role))
                .fetch();

        return users;
    }

    private BooleanExpression eqRole(Role role){
        return role == null ? null : user.role.eq(role);
    }
}
