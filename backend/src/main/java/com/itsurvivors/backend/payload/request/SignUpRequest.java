package com.itsurvivors.backend.payload.request;

import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignUpRequest {
	  @NotBlank
	  @Size(min = 6, max = 20)
	  private String username;

	  @NotBlank
	  @Size(max = 50)
	  @Email
	  private String email;

	  private Set<String> role;

	  @NotBlank
	  @Size(min = 8, max = 20)
	  private String password;
	  
	  @NotBlank
	  @Size(max = 50)
	  private String nombre;
	  
	  @NotBlank
	  @Size(max = 50)
	  private String apellido;

	  public String getUsername() {
	    return username;
	  }

	  public void setUsername(String username) {
	    this.username = username;
	  }

	  public String getEmail() {
	    return email;
	  }

	  public void setEmail(String email) {
	    this.email = email;
	  }

	  public String getPassword() {
	    return password;
	  }

	  public void setPassword(String password) {
	    this.password = password;
	  }

	  public Set<String> getRole() {
	    return this.role;
	  }

	  public void setRole(Set<String> role) {
	    this.role = role;
	  }

	  public String getNombre() {
		return nombre;
	  }

	  public void setNombre(String nombre) {
		this.nombre = nombre;
	  }

	  public String getApellido() {
		return apellido;
	  }

	  public void setApellido(String apellido) {
		this.apellido = apellido;
	  }
}
