package com.projectmanager.repository;

import org.springframework.data.repository.CrudRepository;

import com.projectmanager.model.Disciplina;

public interface DisciplinaRepository extends CrudRepository<Disciplina, String>{

	boolean existsByCodigo(String codigo);

	boolean existsByNome(String nome);

	Disciplina findById(Integer id);

}
