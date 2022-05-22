package com.itsurvivors.backend;

import com.itsurvivors.backend.Model.RoleName;
import com.itsurvivors.backend.Model.User;
import com.itsurvivors.backend.Services.IRoleService;
import com.itsurvivors.backend.Services.IService;
import com.itsurvivors.backend.Model.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
	@Autowired
	private IService<User> userService;

	@Autowired
	private IRoleService<Role> roleService;


	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (roleService.findAll().isEmpty()) {
			roleService.saveOrUpdate(new Role(RoleName.ADMIN.toString()));
			roleService.saveOrUpdate(new Role(RoleName.USER.toString()));
		}

		if (userService.findAll().isEmpty()) {
			User user1 = new User();
			user1.setEmail("test@user.com");
			user1.setNombre("Test");
			user1.setApellido("User");
			user1.setUsername("testuser");
			user1.setRole(roleService.findByName(RoleName.USER.toString()));
			user1.setPassword(new BCryptPasswordEncoder().encode("testuser"));
			userService.saveOrUpdate(user1);

			User user2 = new User();
			user2.setEmail("test@admin.com");
			user2.setNombre("Test");
			user2.setApellido("Admin");
			user2.setUsername("testadmin");
			user2.setRole(roleService.findByName(RoleName.ADMIN.toString()));
			user2.setPassword(new BCryptPasswordEncoder().encode("testadmin"));
			userService.saveOrUpdate(user2);
		}
	}
}