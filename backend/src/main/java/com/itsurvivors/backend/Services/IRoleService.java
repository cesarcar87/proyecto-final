package com.itsurvivors.backend.Services;

public interface IRoleService<T> extends IService<T> {

	T findByName(String name);
}