package com.spring.post.controller;

import com.spring.post.dto.response.GetPostPageResponse;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.post.dto.PostMapper;
import com.spring.post.dto.request.DeletePostRequest;
import com.spring.post.dto.request.RegisterPostRequest;
import com.spring.post.dto.request.UpdatePostRequest;
import com.spring.post.dto.response.DeletePostResponse;
import com.spring.post.dto.response.SimplePostResponse;
import com.spring.post.service.PostService;

import lombok.RequiredArgsConstructor;

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

	@GetMapping("/page")
	public ResponseEntity<GetPostPageResponse> getPostByPage(
			@RequestParam(name = "lastId", required = false) Long lastId,
			@PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable
			) {

		GetPostPageResponse response = postService.getPostByPage(lastId, pageable);
		return ResponseEntity.ok(response);
	}
}
