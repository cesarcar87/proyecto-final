package com.itsurvivors.backend.Model;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "Roles")
public class Role {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String name;

	@OneToMany(targetEntity = User.class, mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<User> users;

	public Role() {
	}

	public Role(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}