package com.spring.post.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.common.config.QuerydslConfig;
import com.spring.post.domain.Post;
import com.spring.post.domain.QPost;
import com.spring.post.dto.request.SimplePostRequest;
import com.spring.post.dto.response.SimplePostResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.spring.post.domain.QPost.post;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<SimplePostResponse> searchPost(SimplePostRequest condition, Pageable pageable) {
        Long lastPostId = condition.postId();

        BooleanExpression whereCondition = (lastPostId != null) ? post.id.lt(lastPostId) : null;

        List<SimplePostResponse> results = queryFactory
                .select(Projections.constructor(SimplePostResponse.class,
                        post.id,
                        post.title,
                        post.content,
                        post.createdAt
                ))
                .from(post)
                .where(whereCondition)
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;
        if (results.size() > pageable.getPageSize()) {
            results.remove(pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }




}
