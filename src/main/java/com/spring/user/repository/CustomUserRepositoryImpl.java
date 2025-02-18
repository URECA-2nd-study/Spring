package com.spring.user.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.user.domain.QUser.user;

@Repository
@AllArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> findAllByRole(Role role) {
        // null이면 전체 조회, 아니면 조건별 조회
        return jpaQueryFactory.selectFrom(user)
            .where(roleCheck(role))
            .fetch();
    }

    private BooleanExpression roleCheck(Role role) {
        return role != null ? user.role.eq(role) : null;
    }
}
