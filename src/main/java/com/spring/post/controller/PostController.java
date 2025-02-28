package com.spring.post.controller;

import com.spring.post.dto.PostMapper;
import com.spring.post.dto.request.DeletePostRequest;
import com.spring.post.dto.request.RegisterPostRequest;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.DeletePostResponse;
import com.spring.post.dto.response.SimplePostListResponse;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/{postId}")
	public ResponseEntity<SimplePostResponse> getPost(@PathVariable("postId") Long postId) {
		SimplePostResponse response = postService.getPost(PostMapper.toSimplePostRequest(postId));

		return ResponseEntity.ok(response);
	}

	@GetMapping("/")
	public ResponseEntity<List<SimplePostResponse>> getPostAll() {
		List<SimplePostResponse> response = postService.getPostAll();

		return ResponseEntity.ok(response);
	}

	@PostMapping("/")
	public ResponseEntity<SimplePostResponse> registerPost(@RequestBody RegisterPostRequest request) {
		SimplePostResponse response = postService.registerPost(request);

		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{postId}")
	public ResponseEntity<SimplePostResponse> updatePost(
		@PathVariable("postId") Long postId,
		@RequestBody UpdatePostRequest request) {
		SimplePostResponse response = postService.updatePost(postId, request);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{postId}")
	public ResponseEntity<DeletePostResponse> deletePost(
		@PathVariable("postId") Long postId,
		@RequestBody DeletePostRequest request) {
		DeletePostResponse response = postService.deletePost(postId, request);

		return ResponseEntity.ok(response);
	}

	// 포스트 페이징 조회
	@GetMapping("/paging")
	public ResponseEntity<SimplePostListResponse> getUserWithPaging(
		Pageable pageable,
		@RequestParam(value = "lastPostId", required = false) Long lastPostId) {
		SimplePostListResponse response = postService.getPostWithPaging(pageable, lastPostId);

		return ResponseEntity.ok(response);
	}

}
