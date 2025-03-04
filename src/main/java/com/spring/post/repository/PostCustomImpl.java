package com.spring.post.repository;

import static com.spring.post.domain.QPost.*;
import static com.spring.user.domain.QUser.*;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.spring.post.dto.response.NoOffsetPostResponse;
import com.spring.post.dto.response.QSimplePostResponse;
import com.spring.post.dto.response.SimplePostResponse;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PostCustomImpl implements PostCustom{

	private final JPAQueryFactory queryFactory;

	static final int PAGE_SIZE = 5;

	@Override
	public NoOffsetPostResponse paginationPostWithNoOffset(Long lastPostId) {

		List<SimplePostResponse> responses = queryFactory.select(new QSimplePostResponse(post, user.name) {
			})
			.from(post)
			.where(paginationCursor(lastPostId))
			.orderBy(post.id.desc())
			.limit(PAGE_SIZE + 1)
			.fetch();

		boolean hasNext = hasNext(responses);

		if(hasNext) {
			responses.remove(PAGE_SIZE);
		}

		return new NoOffsetPostResponse(responses, hasNext);
	}

	private <T> boolean hasNext(List<T> responses) {
		return responses.size() > PAGE_SIZE;
	}

	private BooleanExpression paginationCursor(Long lastPostId) {
		if(Objects.isNull(lastPostId)) {
			return null;
		}

		return post.id.lt(lastPostId);
	}
}
