package com.spring.user.domain;

import com.spring.common.domain.TimeBaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

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

	@Column(name ="point", nullable = false)
	private BigDecimal point;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role;

	@Builder(access = AccessLevel.PRIVATE)
	private User(String email, String password, String name, Role role, BigDecimal point) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.role = role;
		this.point = point;
	}

	public static User of(String email, String password, String name, Role role) {
		return User.builder()
			.email(email)
			.password(password)
			.name(name)
			.role(role)
			.build();
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

	public void updateInfo(String email, String name, String role) {
		this.email = email;
		this.name = name;
		this.role = Role.of(role);
	}

	public void updatePoint(BigDecimal point) {
		this.point = point;
	}
}
