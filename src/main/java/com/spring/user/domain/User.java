package com.spring.user.domain;

import static jakarta.persistence.EnumType.*;

import java.math.BigDecimal;

import com.spring.common.domain.TimeBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

	@Column(name ="point", nullable = false)
	private BigDecimal point;

	@Builder(access = AccessLevel.PRIVATE)
	private User(String email, String password, String name, Role role, BigDecimal point) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role;
		this.point = point;
	}

	public static User of(String email, String password, String name, Role role, BigDecimal point) {
		return User.builder()
			.email(email)
			.password(password)
			.name(name)
			.role(role)
			.point(point)
			.build();
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

	public void updatePoint(BigDecimal point) {
		this.point = this.point.add(point);
	}
}
