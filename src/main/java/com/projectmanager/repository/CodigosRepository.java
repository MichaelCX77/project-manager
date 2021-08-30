package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.CodigoTipoDesc;
import com.projectmanager.model.Codigos;

public interface CodigosRepository extends CrudRepository<Codigos, String>{

	boolean existsByCodigo(String codigo);
	
	Codigos findByCodigo(String codigo);

	Codigos findByUsuarioEmailAndAtivoAndCodigoTipo(String email, char ativo, CodigoTipoDesc tipo);

	Codigos findById(int id);

}
