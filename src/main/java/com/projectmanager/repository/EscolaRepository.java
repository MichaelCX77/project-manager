package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Escola;

public interface EscolaRepository extends CrudRepository<Escola, String>{

	Escola findByCnpj(String cnpj);

}