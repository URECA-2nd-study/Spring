package com.spring.user.repository;

import static com.spring.user.domain.QUser.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findAllUsersByRole(Role role) {

        return queryFactory
                .selectFrom(user)
                .where(user.role.eq(role))
                .fetch();
    }
}
