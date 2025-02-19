package com.spring.user.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.user.domain.QUser;
import com.spring.user.domain.User;
import com.spring.user.dto.request.UserFilterSearchRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<User> searchUsers(UserFilterSearchRequest request) {

        QUser user = QUser.user;
        BooleanBuilder builder = new BooleanBuilder();

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            builder.and(user.email.containsIgnoreCase(request.getEmail()));
        }
        if (request.getName() != null && !request.getName().isEmpty()) {
            builder.and(user.name.containsIgnoreCase(request.getName()));
        }
        if (request.getRole() != null && !request.getRole().isEmpty()) {
            builder.and(user.role.stringValue().eq(request.getRole()));
        }


        return jpaQueryFactory
                .selectFrom(user)
                .where(builder)
                .fetch();
    }
}
