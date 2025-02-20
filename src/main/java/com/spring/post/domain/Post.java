package com.spring.post.domain;

import static jakarta.persistence.ConstraintMode.*;

import com.spring.common.domain.TimeBaseEntity;
import com.spring.user.domain.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;

	@Builder(access = AccessLevel.PRIVATE)
	private Post(String title, String content, User user) {
		this.title = title;
		this.content = content;
		this.user = user;
	}

	public static Post of(String title, String content, User user) {
		return Post.builder()
			.title(title)
			.content(content)
			.user(user)
			.build();
	}

	public void updateInfo(String title, String content) {
		this.title = title;
		this.content = content;
	}
}

