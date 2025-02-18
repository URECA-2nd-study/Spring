package com.spring.user.domain;

import com.spring.common.domain.TimeBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "name", nullable = false)
	private String name;

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
