package com.spring.post.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.post.domain.Post;
import com.spring.post.domain.QPost;
import com.spring.post.dto.response.PagePostResponse;
import com.spring.user.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

import static com.spring.post.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<Post> findAllbyId(Long cursorId, Pageable pageable) {

        QPost post  = QPost.post;
        QUser user = QUser.user;

        List<Post> result = jpaQueryFactory.selectFrom(post).leftJoin(post.user,user).where(eqCursorId(cursorId)).orderBy(post.id.desc()).limit(pageable.getPageSize()+1).fetch();

        boolean hasNext = result.size() > pageable.getPageSize();

        if(hasNext){
            result.remove(result.size()-1);
        }


        return new SliceImpl<>(result, pageable, hasNext);
    }

    private BooleanExpression eqCursorId(Long cursorId) {

        if(cursorId !=null){
            return post.id.lt(cursorId);
        }

        return null;
    }
}
