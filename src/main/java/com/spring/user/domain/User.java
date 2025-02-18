package com.spring.user.domain;

import com.spring.common.domain.TimeBaseEntity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Table(name = "Users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

	@Enumerated(STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Builder(access = AccessLevel.PRIVATE)
	private User(String email, String password, String name, Role role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role =role;
	}

	public static User of(String email, String password, String name, Role role) {
		return User.builder()
			.email(email)
			.password(password)
			.name(name)
			.role(role)
			.build();
	}

	public void updateInfo(String email, String name, String role) {
		this.email = email;
		this.name = name;
		this.role = Role.of(role);
	}
}
