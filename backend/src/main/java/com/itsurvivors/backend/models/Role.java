package com.itsurvivors.backend.models;

import javax.persistence.*;



@Entity
@Table(name="ROLES")
public class Role {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private ERole nombre;
	
	public Role() {

	}

	public Role(ERole nombre) {
		this.nombre = nombre;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ERole getNombre() {
		return nombre;
	}

	public void setNombre(ERole nombre) {
		this.nombre = nombre;
	}
}
