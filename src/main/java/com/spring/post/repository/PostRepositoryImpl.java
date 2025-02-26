package com.spring.post.repository;

import static com.spring.post.domain.QPost.post;
import static com.spring.user.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.post.domain.Post;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Slice<Post> findAllByPage(Long lastId, Pageable pageable) {
        JPAQuery<Post> query = queryFactory
                .selectFrom(post)
                .leftJoin(post.user, user)
                .fetchJoin()
                .where(lastId == null ? null : post.id.lt(lastId))
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1); // hasNext를 확인하기 위해 하나 더 조회

        List<Post> result = query.fetch();

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.remove(result.size() - 1); // hasNext 확인 후 마지막 요소 제거
            hasNext = true;
        }

        return new SliceImpl<>(result, pageable, hasNext);
    }

    @Override
    public boolean exists() {
        return queryFactory
                .selectOne()
                .from(post)
                .fetchFirst() != null;
    }

    @Override
    public void batchInsert(List<Post> posts) {
        String sql = "INSERT INTO post (title, content, user_id, created_at, updated_at) VALUES(?,?,?,?,?)";

        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Post post = posts.get(i);
                ps.setString(1, post.getTitle());
                ps.setString(2, post.getContent());
                ps.setLong(3, post.getUser().getId());
                ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            }

            @Override
            public int getBatchSize() {
                return posts.size();
            }
        });
    }
}
