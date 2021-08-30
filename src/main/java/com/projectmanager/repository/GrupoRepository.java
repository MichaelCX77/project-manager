package com.projectmanager.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Grupo;

public interface GrupoRepository extends CrudRepository<Grupo, String>{

	ArrayList<Grupo> findByUsuario(int usuario);

	List<Grupo> findByIdGrupo(int idGrupo);

	boolean existsByIdAndNome(int id, String nomeGrupo);
	
}