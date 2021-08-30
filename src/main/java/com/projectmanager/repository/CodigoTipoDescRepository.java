 package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.CodigoTipoDesc;

public interface CodigoTipoDescRepository extends CrudRepository<CodigoTipoDesc, String>{

	CodigoTipoDesc findById(char c);

}
