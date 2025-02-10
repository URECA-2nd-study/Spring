package com.spring.post.domain;

import static jakarta.persistence.ConstraintMode.*;

import com.spring.common.domain.TimeBaseEntity;
import com.spring.user.domain.User;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;

	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId",
		nullable = false,
		foreignKey = @ForeignKey(NO_CONSTRAINT))
	private User user;
}
