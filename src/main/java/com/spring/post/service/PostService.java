package com.spring.post.service;

import com.spring.common.exception.runtime.BaseException;
import com.spring.post.domain.Post;
import com.spring.post.dto.PostMapper;
import com.spring.post.dto.request.DeletePostRequest;
import com.spring.post.dto.request.RegisterPostRequest;
import com.spring.post.dto.request.SimplePostRequest;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.DeletePostResponse;
import com.spring.post.dto.response.SimplePostListResponse;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.exception.PostErrorCode;
import com.spring.post.repository.PostRepository;
import com.spring.user.domain.User;
import com.spring.user.exception.UserErrorCode;
import com.spring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostRepository postRepository;
	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public SimplePostResponse getPost(SimplePostRequest postSingleDto) {
		Post post = postRepository.findById(postSingleDto.postId()).orElseThrow(
			() -> new BaseException(PostErrorCode.NOT_FOUND_POST));

		return PostMapper.toSimplePostResponse(post, post.getUser().getName());
	}

	@Transactional(readOnly = true)
	public List<SimplePostResponse> getPostAll() {
		List<Post> posts = postRepository.findAll();

		return PostMapper.toSimplePostResponses(posts);
	}

	@Transactional
	public SimplePostResponse registerPost(RegisterPostRequest request) {
		User findUser = getUser(request.userId());
		Post savedPost = postRepository.save(PostMapper.toPost(request.title(), request.content(), findUser));

		return PostMapper.toSimplePostResponse(savedPost, findUser.getName());
	}

	@Transactional
	public SimplePostResponse updatePost(Long postId, UpdatePostRequest request) {
		Post findPost = getPost(postId);
		User author = findPost.getUser();		// fetch join으로 한 번에 가져올까 고민됨..

		validateAuthor(author, request.userId());

		findPost.updateInfo(request.title(), request.content());

		return PostMapper.toSimplePostResponse(findPost, author.getName());
	}

	public DeletePostResponse deletePost(Long postId, DeletePostRequest request) {
		Post findPost = getPost(postId);
		User author = findPost.getUser();

		validateAuthor(author, request.userId());

		postRepository.delete(findPost);

		return PostMapper.toDeletePostResponse();
	}

	@Transactional(readOnly = true)
	public SimplePostListResponse getPostWithPaging(Pageable pageable, Long lastPostId) {
		List<Post> findPostAll = postRepository.findAllByPageable(pageable, lastPostId);
		lastPostId = findPostAll.get(findPostAll.size()-1).getId();

		return PostMapper.toSimplePostListResponse(findPostAll, lastPostId);
	}

	private void validateAuthor(User author, Long userId) {
		if(author.getId() != userId) {		// equals 오버라이딩을 하고 싶은데 잘 못해서 패스..
			throw new BaseException(PostErrorCode.POST_FORBIDDEN);
		}
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow(
			() -> new BaseException(UserErrorCode.NOT_FOUND_USER)
		);
	}

	private Post getPost(Long postId) {
		return postRepository.findById(postId).orElseThrow(
			() -> new BaseException(PostErrorCode.NOT_FOUND_POST)
		);
	}
}
