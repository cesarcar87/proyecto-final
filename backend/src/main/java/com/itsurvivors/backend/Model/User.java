package com.itsurvivors.backend.Model;

import javax.persistence.*;
 
@Entity
@Table(name = "Usuarios")
public class User {
	@Id
	@GeneratedValue
	private Long id;
	@Column(nullable = false)
	private String nombre;
	@Column(nullable = false)
	private String apellido;
	@Column(nullable = false)
	private String email;
	@Column(nullable = false)
	private String username;
	@Column(nullable = false)
	private String password;
	@ManyToOne
	@JoinColumn(name = "role_id")
	private Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido= apellido;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username= username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}