package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Classe;

public interface ClasseRepository extends CrudRepository<Classe, String>{

	Classe findById(Integer idClasse);

}
