package com.spring.user.domain;

import com.spring.common.domain.TimeBaseEntity;
import jakarta.persistence.*;

@Table(name = "tb_user")
@Entity(name = "UserEntity")
public class User extends TimeBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;

	private String password;

	private String name;

	private Role role;
}
