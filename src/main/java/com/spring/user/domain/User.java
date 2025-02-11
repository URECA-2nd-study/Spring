package com.spring.user.domain;

import com.spring.common.domain.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_user")
@NoArgsConstructor
public class User extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;

	private String password;

	private String name;

	@Enumerated(EnumType.STRING)
	private Role role;
}

