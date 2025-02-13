package com.spring.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.spring.post.domain.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	@Query("SELECT p FROM PostEntity p JOIN FETCH p.user")
	List<Post> findAll();
}
