package com.itsurvivors.backend.Services.Impl;

import java.util.Collection;
import java.util.Optional;

import com.itsurvivors.backend.Model.User;
import com.itsurvivors.backend.Repo.UserRepo;
import com.itsurvivors.backend.Services.IService;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServImp implements IService<User> {

	@Autowired
	private UserRepo userRepo;

	@Override
	public Collection<User> findAll() {
		return userRepo.findAll();
	}

	@Override
	public Optional<User> findById(Long id) {
		return userRepo.findById(id);
	}

	@Override
	public User saveOrUpdate(User user) {
		return userRepo.saveAndFlush(user);
	}

	@Override
	public String deleteById(Long id) {
		JSONObject jsonObject = new JSONObject();
		try {
			userRepo.deleteById(id);
			jsonObject.put("message", "User deleted successfully");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}

}