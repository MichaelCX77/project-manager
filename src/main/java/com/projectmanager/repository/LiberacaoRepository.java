package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Codigos;
import com.projectmanager.model.Liberacao;

public interface LiberacaoRepository extends CrudRepository<Liberacao, String>{

	Liberacao findByCodigo(Codigos findById);

}
