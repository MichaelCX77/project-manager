package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.PerfilDesc;

public interface PerfilDescRepository extends CrudRepository<PerfilDesc, String>{

	PerfilDesc findById(int id);

}
