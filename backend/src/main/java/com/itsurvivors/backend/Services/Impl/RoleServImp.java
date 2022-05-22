package com.itsurvivors.backend.Services.Impl;

import java.util.Collection;
import java.util.Optional;

import com.itsurvivors.backend.Model.Role;
import com.itsurvivors.backend.Repo.RoleRepo;
import com.itsurvivors.backend.Services.IRoleService;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServImp implements IRoleService<Role> {

	@Autowired
	private RoleRepo roleRepo;

	@Override
	public Collection<Role> findAll() {
		return roleRepo.findAll();
	}

	@Override
	public Optional<Role> findById(Long id) {
		return roleRepo.findById(id);
	}
	
	@Override
	public Role findByName(String name) {
		return roleRepo.findByName(name);
	}

	@Override
	public Role saveOrUpdate(Role role) {
		return roleRepo.saveAndFlush(role);
	}

	@Override
	public String deleteById(Long id) {
		JSONObject jsonObject = new JSONObject();
		try {
			roleRepo.deleteById(id);
			jsonObject.put("message", "Role deleted successfully");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

}