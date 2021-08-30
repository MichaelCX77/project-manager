package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Unidade;

public interface UnidadeRepository extends CrudRepository<Unidade, String>{

	boolean existsByCep(String cep);

	boolean existsByNome(String nome);

	Unidade findByCep(String cep);

	Unidade findById(int id);

}