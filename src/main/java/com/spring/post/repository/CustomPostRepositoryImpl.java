package com.spring.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.post.domain.Post;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.post.domain.QPost.post;

@Repository
@AllArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * Pageable 구현
     */
    @Override
    public List<Post> findAllByPageable(Pageable pageable, Long lastPostId) {
        return jpaQueryFactory
            .selectFrom(post)
            .orderBy(post.id.desc())
            .where(ltPostId(lastPostId))
            .limit(pageable.getPageSize())
            .fetch();
    }

    private BooleanExpression ltPostId(Long lastPostId) {
        if(lastPostId == null) return null;
        return post.id.lt(lastPostId);
    }
}
