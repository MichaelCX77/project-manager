package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.LiberacaoSiglasDesc;

public interface LiberacaoSiglasDescRepository extends CrudRepository<LiberacaoSiglasDesc, String>{

	LiberacaoSiglasDesc findByTipo(char c);

}
