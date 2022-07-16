package com.itsurvivors.backend.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itsurvivors.backend.models.ERole;
import com.itsurvivors.backend.models.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(ERole nombre);

}
