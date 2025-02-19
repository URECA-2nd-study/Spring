package com.spring.user.repository;

import static com.spring.user.domain.QUser.user;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.user.domain.Role;
import com.spring.user.domain.User;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findAllByRole(Role role) {
        List<User> users = queryFactory
                .selectFrom(user)
                .where(eqRole(role))
                .fetch();

        return users;
    }

    @Override
    public boolean exists() {
        return queryFactory
                .selectOne()
                .from(user)
                .fetchFirst() != null;
    }

    @Override
    public void batchInsert(List<User> users) {
        String sql = "INSERT INTO users (email, password, name, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                User user = users.get(i);
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getName());
                ps.setString(4, user.getRole().name());
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return users.size();
            }
        });
    }

    private BooleanExpression eqRole(Role role){
        return role == null ? null : user.role.eq(role);
    }
}