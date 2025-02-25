package com.spring.post.repository;

import static com.spring.post.domain.QPost.post;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.post.domain.Post;
import com.spring.post.dto.PostMapper;
import com.spring.post.dto.response.PagePostResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Slice<PagePostResponse> searchPostPageBasic(Long postId, Pageable pageable) {
        List<Post> result = queryFactory
                .selectFrom(post)
                .where(postIdLt(postId))
                .orderBy(post.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        Long lastPostId = result.size() > pageable.getPageSize() - 1 ? result.get(pageable.getPageSize() - 1).getId() : null;

        List<PagePostResponse> content = PostMapper.toPagePostResponses(result, lastPostId);

        boolean hasNext = false;
        if (result.size() > pageable.getPageSize()) {
            result.subList(0, pageable.getPageSize());
            hasNext = true;
        }

        return new SliceImpl<>(content, pageable, hasNext);
    }

    private BooleanExpression postIdLt(Long postId) {
        return postId != null ? post.id.lt(postId) : null;
    }
}
